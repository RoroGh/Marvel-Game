package model.effects;

import model.world.Champion;

public class Shield extends Effect implements Cloneable{

	public Shield(int duration) {
		super("Shield", duration, EffectType.BUFF);

	}

	public void apply(Champion c) {
		// TODO Auto-generated method stub
		c.setSpeed((int) (c.getSpeed() * 1.02));

	}

	@Override
	public void remove(Champion c) {
		c.setSpeed((int) (c.getSpeed() / 1.02));

	}

	@Override
	public Effect clone()throws CloneNotSupportedException {
		Shield temp = new Shield(getDuration());
		return temp;
	}

}
