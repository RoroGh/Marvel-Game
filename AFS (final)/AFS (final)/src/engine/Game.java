package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import exceptions.*;
import model.abilities.*;
import model.effects.*;
import model.world.*;

public class Game {
	private static ArrayList<Champion> availableChampions = new ArrayList<Champion>();
	private static ArrayList<Ability> availableAbilities = new ArrayList<Ability>();
	private static ArrayList<ImageView> availableImages = new ArrayList<ImageView>();
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;
	
	

	public Game(Player first, Player second) {

		firstPlayer = first;
		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		prepareChampionTurns();
		placeChampions();
		placeCovers();
	}
	
	public static void loadImages(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			ImageView img = new ImageView(line);
			img.setPreserveRatio(true);
			img.setFitHeight(200);
			availableImages.add(img);
			line = br.readLine();
		}
		br.close();
	}
	
	public static void loadIcons(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		int i = 0;
		while (line != null) {
			Image img = new Image(line);
			availableChampions.get(i).setIcon(img);
			i++;
			line = br.readLine();
		}
		br.close();
	}


	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		
		int i = 0;
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}
	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				
				i++;
			}
		}
	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}

	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}
	
	public static ArrayList<ImageView> getAvailableImages() {
		return availableImages;
	}

	public Player checkGameOver() {
		if (firstPlayer.getTeam().isEmpty())
			return secondPlayer;
		if (secondPlayer.getTeam().isEmpty())
			return firstPlayer;
		return null;

	}

	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		Champion curr = getCurrentChampion();
		if (curr.getCurrentActionPoints() == 0)
			throw new NotEnoughResourcesException("Not Enough AP. You're poor!");

		if (curr.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Too bad you're rooted XP");

		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - 1);

		int x = (int) curr.getLocation().getX();
		int oldx = x;
		int y = (int) curr.getLocation().getY();
		int oldy = y;
		switch (d) {
		case UP:
			++x;
			break;
		case DOWN:
			--x;
			break;
		case RIGHT:
			++y;
			break;
		case LEFT:
			--y;
			break;
		}
		if (x >= 5 || y >= 5 || x < 0 || y < 0)
			throw new UnallowedMovementException("3ala fen?!!");
		if (board[x][y] != null)
			throw new UnallowedMovementException("Can't you see it's not empty?!");
	
		curr.setLocation(new Point(x, y));
		board[oldx][oldy] = null;
		board[x][y] = curr;
	}

	public void attack(Direction d) throws NotEnoughResourcesException, ChampionDisarmedException {
		Champion curr = getCurrentChampion();
		int AR = curr.getAttackRange();
		int x = (int) curr.getLocation().getX();
		int y = (int) curr.getLocation().getY();
		
		if (curr.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("Not Enough AP. You're poor!");
		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - 2);

		if (curr.checkEffect("Disarm"))
			throw new ChampionDisarmedException("Sorry Champ disarmed :(");

		int DMG = curr.getAttackDamage();
		int specialDMG = (int) (1.5 * DMG);

		for (int i = 0; i < AR; i++) {
			if (d == Direction.UP)
				x++;
			else if (d == Direction.DOWN)
				x--;
			else if (d == Direction.LEFT)
				y--;
			else if (d == Direction.RIGHT)
				y++;

			if (isValidNeg(curr, x, y)) {

				Damageable target = (Damageable) board[x][y];
				if (target instanceof Champion && ((Champion) target).checkEffect("Shield")) {
					int index = ((Champion) target).findEffect("Shield");
					Effect e= ((Champion) target).getAppliedEffects().remove(index);
					e.remove((Champion) target);
					return;
				}else if (target instanceof Champion && ((Champion) target).checkEffect("Dodge")) {
					int random = (int) (Math.random() * 2);
					if (random == 0)
						return;
				}
				
				if (curr instanceof Hero) {
					if (target instanceof Villain || target instanceof AntiHero)
						target.setCurrentHP((int) (target.getCurrentHP() - specialDMG));
					else
						target.setCurrentHP(target.getCurrentHP() - DMG);
				} else if (curr instanceof Villain) {
					if (target instanceof Hero || target instanceof AntiHero)
						target.setCurrentHP((int) (target.getCurrentHP() - specialDMG));
					else
						target.setCurrentHP(target.getCurrentHP() - DMG);
				} else {
					if (target instanceof AntiHero || target instanceof Cover)
						target.setCurrentHP(target.getCurrentHP() - DMG);
					else
						target.setCurrentHP((int) (target.getCurrentHP() - specialDMG));

				}

				checkCond(target);
				checkGameOver();
				return;

			}
		}

	}
	
	private boolean isValidInter(Champion c, int x, int y) {
		if (x >= BOARDWIDTH || y >= BOARDHEIGHT || x < 0 || y < 0) 
			return false;

		if (board[x][y] == null)
			return false;
		
		Damageable target = (Damageable) board[x][y];

		if (target instanceof Cover)
			return false;

		if ((firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(target))
				|| (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(target)))
			return false;

		return true;
	}

	private boolean isValidNeg(Champion c, int x, int y) {
		if (x >= BOARDWIDTH || y >= BOARDHEIGHT || x < 0 || y < 0) {
			return false;
			
		}
		if (board[x][y] == null)
			return false;

		Damageable target = (Damageable) board[x][y];

		if ((firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(target))
				|| (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(target)))
			return false;

		return true;

	}

	private boolean isValidPos(Champion c, int x, int y) {
		if (x >= BOARDWIDTH || y >= BOARDHEIGHT || x < 0 || y < 0) 
			return false;

		if (board[x][y] == null)
			return false;
		
		Damageable target = (Damageable) board[x][y];

		if (target instanceof Cover)
			return false;

		if ((firstPlayer.getTeam().contains(c) && firstPlayer.getTeam().contains(target))
				|| (secondPlayer.getTeam().contains(c) && secondPlayer.getTeam().contains(target)))
			return true;

		return false;

	}

	private int manhattan(Point a, Point b) {
		return (int) (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
	}

	private boolean abilityCanBeCast(Champion curr, Ability a) {
		ArrayList<Ability> abilities = curr.getAbilities();
		for (Ability i : abilities) {
			if (i.getName().equals(a.getName())) {
				if (i.getCurrentCooldown() > 0)
					return false;
				i.setCurrentCooldown(a.getBaseCooldown());
				return true;
			}
		}
		return false;
	}
	
	private boolean isAbilityPositive(Ability a) {
		return (a instanceof HealingAbility || (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF));
	}

	public void castAbility(Ability a) throws CloneNotSupportedException, NotEnoughResourcesException, AbilityUseException {
		Champion curr = getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<>();

		if (curr.getCondition() == Condition.INACTIVE || curr.getCondition()==Condition.KNOCKEDOUT)
			throw new AbilityUseException("Champ Not Active!");

		if (curr.getCurrentActionPoints() - a.getRequiredActionPoints() < 0 || curr.getMana() - a.getManaCost() < 0)
			throw new NotEnoughResourcesException("Not Enough Resources! u peasant!");

		if (!(abilityCanBeCast(curr, a)))
			throw new AbilityUseException("Sorry champ, can't use that yet :(");
		
		if(curr.checkEffect("Silence"))
			throw new AbilityUseException("'I won't be silenced'.. Except sorry u are XP");

		if (a.getCastArea() == AreaOfEffect.SELFTARGET)
			targets.add(curr);

		else if (a.getCastArea() == AreaOfEffect.TEAMTARGET) {
			if(isAbilityPositive(a)) {
				if ((firstPlayer.getTeam().contains(curr))) {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						if (manhattan(curr.getLocation(), firstPlayer.getTeam().get(i).getLocation()) <= a.getCastRange())
							targets.add(firstPlayer.getTeam().get(i));
					}
				} else {
					for (int i = 0; i < secondPlayer.getTeam().size(); i++)
						if (manhattan(curr.getLocation(), secondPlayer.getTeam().get(i).getLocation()) <= a.getCastRange())
							targets.add(secondPlayer.getTeam().get(i));
				}
			}
			else {
				if ((secondPlayer.getTeam().contains(curr))) {
					for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
						if (manhattan(curr.getLocation(), firstPlayer.getTeam().get(i).getLocation()) <= a.getCastRange())
							targets.add(firstPlayer.getTeam().get(i));
					}
				} else {
					for (int i = 0; i < secondPlayer.getTeam().size(); i++)
						if (manhattan(curr.getLocation(), secondPlayer.getTeam().get(i).getLocation()) <= a.getCastRange())
							targets.add(secondPlayer.getTeam().get(i));
				}
			}
		}

		else if (a.getCastArea() == AreaOfEffect.SURROUND) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					int x = curr.getLocation().x + i;
					int y = curr.getLocation().y + j;

					if (!(i == 0 && j == 0)) {
						if ((a instanceof HealingAbility) || (a instanceof CrowdControlAbility
								&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
							if (isValidPos(curr, x, y))
								targets.add((Damageable) board[x][y]);
						} else if ((a instanceof DamagingAbility)) {
							if (isValidNeg(curr, x, y) )
								targets.add((Damageable) board[x][y]);}
						else if(a instanceof CrowdControlAbility
									&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF) {
								if (isValidInter(curr, x, y))
									targets.add((Damageable) board[x][y]);
						}
					}
				}
			}
		}
		
		a.execute(targets);
		checkCond(targets);
		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - a.getRequiredActionPoints());
		curr.setMana(curr.getMana() - a.getManaCost());
		checkGameOver();
	}

	public void castAbility(Ability a, Direction d) throws CloneNotSupportedException, AbilityUseException, NotEnoughResourcesException {
		Champion curr = getCurrentChampion();
		ArrayList<Damageable> targets = new ArrayList<>();
		
		if (!a.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
			return;
		
		if (curr.getCurrentActionPoints() - a.getRequiredActionPoints() < 0 || curr.getMana() - a.getManaCost() < 0)
			throw new NotEnoughResourcesException("Not Enough Resources! u peasant!");
		
		if (curr.getCondition() == Condition.INACTIVE || curr.getCondition()==Condition.KNOCKEDOUT)
			throw new AbilityUseException("Champ Not Active!");
		
		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - a.getRequiredActionPoints());
		curr.setMana(curr.getMana() - a.getManaCost());
		
		if(curr.checkEffect("Silence"))
			throw new AbilityUseException("'I won't be silenced'.. Except sorry u are XP");
		
		if (!(abilityCanBeCast(curr, a)))
			throw new AbilityUseException("Sorry champ, can't use that yet :(");
		
		int x = curr.getLocation().x;
		int y = curr.getLocation().y;
		
		for (int i = 0; i < a.getCastRange(); i++) {
			if (d == Direction.UP)
				x++;
			else if (d == Direction.DOWN)
				x--;
			else if (d == Direction.LEFT)
				y--;
			else if (d == Direction.RIGHT)
				y++;
			
			if ((a instanceof HealingAbility) || (a instanceof CrowdControlAbility
					&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
				if (isValidPos(curr, x, y))
					targets.add((Damageable) board[x][y]);
			} else if ((a instanceof DamagingAbility)) {
				if (isValidNeg(curr, x, y))
					targets.add((Damageable) board[x][y]);}
			else if(a instanceof CrowdControlAbility
						&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF) {
					if (isValidInter(curr, x, y))
						targets.add((Damageable) board[x][y]);
			}
		}
		
		a.execute(targets);
		checkCond(targets);
//		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - a.getRequiredActionPoints());
//		curr.setMana(curr.getMana() - a.getManaCost());
		checkGameOver();
	}

	public void castAbility(Ability a, int x, int y) throws CloneNotSupportedException, NotEnoughResourcesException, AbilityUseException, InvalidTargetException {

		ArrayList<Damageable> targets = new ArrayList<>();
		Champion curr = getCurrentChampion();
		
		if (!a.getCastArea().equals(AreaOfEffect.SINGLETARGET))
			return;
		
		if (curr.getCurrentActionPoints() - a.getRequiredActionPoints() < 0 || curr.getMana() - a.getManaCost() < 0)
			throw new NotEnoughResourcesException("Not Enough Resources! u peasant!");
		
		curr.setMana(curr.getMana() - a.getManaCost());
		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - a.getRequiredActionPoints());
		
		if(curr.checkEffect("Silence"))
			throw new AbilityUseException("'I won't be silenced'.. Except sorry u are XP");
		
		if (curr.getCondition() == Condition.INACTIVE || curr.getCondition()==Condition.KNOCKEDOUT)
			throw new AbilityUseException("Champ Not Active!");
		
		if (!(abilityCanBeCast(curr, a)))
			throw new AbilityUseException("Sorry champ, can't use that yet :(");
		
		if(board[x][y] ==null) {
			throw new InvalidTargetException("it's an empty cell yunno XD");
		}
		
		if (manhattan(curr.getLocation(), new Point(x, y)) > a.getCastRange())
			throw new AbilityUseException("Out of Range!");

		if ((a instanceof HealingAbility) || (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF)) {
			if (isValidPos(curr, x, y))
				targets.add((Damageable) board[x][y]);
			else {
				throw new InvalidTargetException("Invalid bro wth!!");
			}
		} 
		else if (a instanceof DamagingAbility) {
			if (isValidNeg(curr, x, y)) 
				targets.add((Damageable) board[x][y]);
			else 
				throw new InvalidTargetException("Invalid bro wth!!");
				
		}
		else if (a instanceof CrowdControlAbility
				&& ((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF){
			if (isValidInter(curr, x, y))
				targets.add((Damageable) board[x][y]);
			else
				throw new InvalidTargetException("Invalid bro wth!!");
		}

		a.execute(targets);
		checkCond(targets);
//		curr.setMana(curr.getMana() - a.getManaCost());
//		curr.setCurrentActionPoints(curr.getCurrentActionPoints() - a.getRequiredActionPoints());
		checkGameOver();

	}

	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException  {
		Champion curr = getCurrentChampion();
		Player currPlayer = (firstPlayer.getTeam().contains(curr)) ? firstPlayer : secondPlayer;
		Player otherPlayer = (currPlayer.equals(firstPlayer)) ? secondPlayer : firstPlayer;
		
		boolean leaderAbility = (currPlayer.equals(firstPlayer)) ? firstLeaderAbilityUsed : secondLeaderAbilityUsed;
		
		if (!(curr.equals(currPlayer.getLeader())))
			throw new LeaderNotCurrentException("You are not a leader!");
		
		else if (leaderAbility)
			throw new LeaderAbilityAlreadyUsedException("it's only used once bala4 eftra!");
		
		if (currPlayer.equals(firstPlayer))
			firstLeaderAbilityUsed = true;
		else
			secondLeaderAbilityUsed = true;
		
		ArrayList<Champion> targets = new ArrayList<>();
		
		if (curr instanceof Hero)
			targets = currPlayer.getTeam();
		
		else if (curr instanceof Villain) {
			for(Champion t:otherPlayer.getTeam()) {
				if(t.getCurrentHP()<0.3*t.getMaxHP())
					targets.add(t);
			}
		}
		
		else if (curr instanceof AntiHero) {
			targets.addAll(firstPlayer.getTeam());
			targets.addAll(secondPlayer.getTeam());
			targets.remove(firstPlayer.getLeader());
			targets.remove(secondPlayer.getLeader());
		}
		
		curr.useLeaderAbility(targets);
		for(Champion c: targets) {
			checkCond((Damageable)c);
		}
		
		checkGameOver();
	}

	

	public void endTurn() {
		if (checkGameOver() != null)
			return;
		
		turnOrder.remove();

		if (turnOrder.isEmpty())
			prepareChampionTurns();
		
		Champion curr = (Champion) turnOrder.peekMin();
		Condition currCond = curr.getCondition(); 
		
//		for (int i = 0; i < curr.getAppliedEffects().size(); i++) {
//			Effect e = curr.getAppliedEffects().get(i);
//			e.setDuration(Math.max(e.getDuration() - 1,0));
//			if (e.getDuration() == 0) {
//				curr.getAppliedEffects().remove(e);
//				e.remove(curr);
//			}
//		}
		
		int i = 0;
		while ( i < curr.getAppliedEffects().size()) {
			Effect e = curr.getAppliedEffects().get(i);
			e.setDuration(Math.max(e.getDuration() - 1,0));
			if (e.getDuration() == 0) {
				curr.getAppliedEffects().remove(e);
				e.remove(curr);
			}
			else 
				i++;
		}

		for (Ability a : curr.getAbilities())
			a.setCurrentCooldown(Math.max(a.getCurrentCooldown() - 1, 0));

		if ((currCond == Condition.INACTIVE) || (currCond == Condition.KNOCKEDOUT)) {
			endTurn();
			return;
		} else {
			curr.setCurrentActionPoints(curr.getMaxActionPointsPerTurn());
		}


	}

	public void prepareChampionTurns() {
		for (Champion curr : firstPlayer.getTeam()) {
			if (curr.getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(curr);
		}
		for (Champion curr : secondPlayer.getTeam()) {
			if (curr.getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(curr);
		}
	}
	
	private void checkCond(Damageable d) {
		if ((d.getCurrentHP() <= 0) || 
		((d instanceof Champion) && ((Champion)d).getCondition() == Condition.KNOCKEDOUT)) {
			int x = d.getLocation().x;
			int y = d.getLocation().y;
			board[x][y] = null;
			if (d instanceof Champion) {
				
				((Champion) d).setCondition(Condition.KNOCKEDOUT);
				
				int s = turnOrder.size();
				PriorityQueue temp = new PriorityQueue (s);
				
				for (int i = 0; i < s; i++) {
					Champion c = (Champion) turnOrder.remove();
					if (c != d)
						temp.insert(c);
				}
				
				while (!temp.isEmpty())
				{
					turnOrder.insert(temp.remove());
				}
				
				if (firstPlayer.getTeam().contains(d))
				{
					int index = firstPlayer.getTeam().indexOf(d);
					firstPlayer.getTeam().remove(d);
					firstPlayer.getImages().remove(index);
				}
				else
				{
					int index = secondPlayer.getTeam().indexOf(d);
					secondPlayer.getTeam().remove(d);
					secondPlayer.getImages().remove(index);
				}
			}

		}
	}

	private void checkCond(ArrayList<Damageable> d) {
		for (Damageable i: d)
			checkCond(i);
	}

}
