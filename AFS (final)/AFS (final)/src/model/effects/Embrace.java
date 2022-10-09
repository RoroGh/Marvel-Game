package model.effects;

import model.world.Champion;

public class Embrace extends Effect implements Cloneable{
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	
	public void apply(Champion c) {
		c.setCurrentHP((int) (c.getCurrentHP()+0.2*c.getMaxHP()));  //Permanent
		c.setMana((int) (c.getMana()*1.2));                             //Permanent
		c.setSpeed((int) (c.getSpeed()*1.2));
		c.setAttackDamage((int) (c.getAttackDamage()*1.2));
	}

	@Override
	public void remove(Champion c) {
		
		c.setSpeed((int) (c.getSpeed()/1.2));
		c.setAttackDamage((int) (c.getAttackDamage()/1.2));
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		Embrace temp = new Embrace (getDuration());
		return temp;
	}

}