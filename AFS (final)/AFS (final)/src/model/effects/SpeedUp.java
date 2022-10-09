package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect implements Cloneable{

	public SpeedUp(int duration) {
		super("SpeedUp", duration, EffectType.BUFF);
	}

	public void apply(Champion c) {
		c.setSpeed((int) (c.getSpeed() * 1.15));
		c.setCurrentActionPoints(c.getCurrentActionPoints() + 1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() + 1);

	}

	@Override
	public void remove(Champion c) {
		c.setSpeed((int) (c.getSpeed() / 1.15));
		c.setCurrentActionPoints(Math.max(0,c.getCurrentActionPoints() - 1));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() - 1);
	
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		SpeedUp temp = new SpeedUp(getDuration());
		return temp;
	}

}
