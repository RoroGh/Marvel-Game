package model.world;

import java.util.ArrayList;

import model.effects.*;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (int i = 0; i < targets.size(); i++) {

			if (targets.get(i).getCondition() != Condition.KNOCKEDOUT) {
				Stun aa = new Stun(2);
				targets.get(i).getAppliedEffects().add(aa);
				aa.apply(targets.get(i));
			}

		}
	}
}
