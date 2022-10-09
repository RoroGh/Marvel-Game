package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect implements Cloneable {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
		
	}

	@Override
	public void remove(Champion c) {
		
		c.setCondition(Condition.ACTIVE);
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if (c.getAppliedEffects().get(i).getName().equals("Stun")) {
				c.setCondition(Condition.INACTIVE);
				return;
			} else if (c.getAppliedEffects().get(i).getName().equals("Root")) {
				c.setCondition(Condition.ROOTED);
			}
		}
		
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		Stun temp = new Stun(getDuration());
		return temp;
	}
}
