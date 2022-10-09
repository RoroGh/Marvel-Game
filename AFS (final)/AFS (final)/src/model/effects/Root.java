package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect implements Cloneable{

	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);

	}

	public void apply(Champion c) {
		if (c.getCondition() != Condition.INACTIVE)
			c.setCondition(Condition.ROOTED);
	}

	@Override
	public void remove(Champion c) {
		
		
		if(c.getCondition()==Condition.INACTIVE ||c.getCondition()==Condition.KNOCKEDOUT)
			  return;
		
		c.setCondition(Condition.ACTIVE);
		
		for( Effect e: c.getAppliedEffects()) {
			if(e.getName().equals("Root")) {
				//rootC++;
				c.setCondition(Condition.ROOTED);
		}
	}
		for( Effect e: c.getAppliedEffects()) {
			if(e.getName().equals("Stun"))
				c.setCondition(Condition.INACTIVE);
		}

	
	
	}

	@Override
	public Object clone()throws CloneNotSupportedException {
		//Root temp = new Root(getDuration());
		return  super.clone();
	}
}
