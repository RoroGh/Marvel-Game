package model.effects;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect implements Cloneable {
	

	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
		
		DamagingAbility Punch = new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SINGLETARGET, 1, 50);
		c.getAbilities().add(Punch);
		
	}

	@Override
	public void remove(Champion c) {
		
		for (int i = 0; i < c.getAbilities().size(); i++)
		{
			Ability a = c.getAbilities().get(i);
			if (a.getName().equals("Punch"))
			{
				c.getAbilities().remove(i);
				break;
			}
		}
		
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		Disarm temp = new Disarm (getDuration());
		return temp;
	}
	
	
	 
	
}
