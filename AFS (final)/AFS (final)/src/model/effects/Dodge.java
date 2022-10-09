package model.effects;

import model.world.Champion;

public class Dodge extends Effect implements Cloneable{

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);

	}

	public void apply(Champion c) {

		c.setSpeed((int) (c.getSpeed() * 1.05));
//random
	}

	@Override
	public void remove(Champion c) {
		c.setSpeed((int) (c.getSpeed() / 1.05));
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		Dodge temp = new Dodge(getDuration());
		return temp;
	}

}
