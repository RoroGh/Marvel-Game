package model.world;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import engine.Player;
import model.abilities.Ability;
import model.effects.Effect;

public abstract class Champion implements Damageable,Comparable{
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	private Image icon;
	
		
	public Champion(String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = this.maxHP;
		this.maxActionPointsPerTurn = actions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.currentActionPoints=maxActionPointsPerTurn;
	}
	
    public abstract void useLeaderAbility(ArrayList<Champion> targets);
	
	public int compareTo(Object o) {
		Champion c = (Champion)o;
			if(this.speed>c.speed)
		return -1;
			else if(this.speed<c.speed)
				return 1;
			return name.compareTo(c.name);
	}
	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}
	
	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public void setCurrentHP(int hp) {

		if (hp < 0) {
			currentHP = 0;
			
		} 
		else if (hp > maxHP)
			currentHP = maxHP;
		else
			currentHP = hp;

	}

	
	public int getCurrentHP() {

		return currentHP;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int currentSpeed) {
		if (currentSpeed < 0)
			this.speed = 0;
		else
			this.speed = currentSpeed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point currentLocation) {
		this.location = currentLocation;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints>maxActionPointsPerTurn)
			currentActionPoints=maxActionPointsPerTurn;
		else 
			if(currentActionPoints<0)
			currentActionPoints=0;
		this.currentActionPoints = currentActionPoints;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	
	public boolean checkEffect (String s)
	{
		for (int i = 0; i < appliedEffects.size(); i++)
			if (s.equals(appliedEffects.get(i).getName()))
				return true;
		return false;
	}
	
	public int findEffect (String s)
	{
		for (int i = 0; i < appliedEffects.size(); i++)
			if (s.equals(appliedEffects.get(i).getName()))
				return i;
		return -1;
	}
	
	public String toString() {
		return  "\n MaxHP: " + maxHP + "\n"+" Mana: " + mana
				+ "\n MaxActionPointsPerTurn: " + maxActionPointsPerTurn
				+ "\n AttackRange: " + attackRange + "\n AttackDamage: "
				+ attackDamage + "\n Speed:" + speed + "\n \n Abilities: \n"
				+ abilities;
	}
	
	public String displayMe4Playing	() {
		return  "\n\n Type: " +
		((this  instanceof Hero)? "Hero":((this  instanceof Villain)? "Villain" : "Anti Hero"))
		+ "\n current HP: " + currentHP + "\n"+" Mana:" + mana
				+ "\n MaxActionPointsPerTurn: \n" + maxActionPointsPerTurn
				+ "\n Attack Range: " + attackRange + "\n Attack Damage: "
				+ attackDamage + "\n Speed: " + speed 
				+ "\n Applied Effects: \n" + appliedEffects +"\n";
	}
	
	public String displayPlaying() {
		return "\n\n Type: " +
		((this  instanceof Hero)? "Hero":((this  instanceof Villain)? "Villain" : "Anti Hero"))
		+ "\n Current HP: " + currentHP + "\n" + " Mana: " + mana
				+ "\n Current Action Points: " + currentActionPoints
				+ "\n Attack Range: " + attackRange + "\n Attack Damage: "
				+ attackDamage + "\n Applied Effects: \n" + appliedEffects + "\n" ;
		//	+" Abilities: "+ abilities + "\n";
	}
	

}
