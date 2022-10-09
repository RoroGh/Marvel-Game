package model.world;

import java.util.ArrayList;
import model.effects.*;


public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (int i = 0; i < targets.size(); i++) {
			ArrayList<Effect> curr = targets.get(i).getAppliedEffects();
			int j=0;
			while(j<curr.size()) {
				if (curr.get(j).getType()==EffectType.DEBUFF) {
					Effect e= curr.remove(j);
					e.remove(targets.get(i));		
				}
				else {
					j++;
				}
			}
			Embrace a = new Embrace(2);
			curr.add(a);
			a.apply(targets.get(i));
		}
	
	}
}

