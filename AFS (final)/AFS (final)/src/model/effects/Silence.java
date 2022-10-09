package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Silence extends Effect implements Cloneable{

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}
	
	public void apply(Champion c) {
		c.setCurrentActionPoints(c.getCurrentActionPoints()+2);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);	
	}

	@Override
	public void remove(Champion c) {
		c.setCurrentActionPoints(Math.max(c.getCurrentActionPoints()-2,0));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);	
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		Silence temp = new Silence (getDuration());
		return temp;
	}

}
