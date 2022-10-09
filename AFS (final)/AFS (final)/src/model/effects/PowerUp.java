package model.effects;

import model.abilities.*;
import model.world.Champion;

public class PowerUp extends Effect implements Cloneable{

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);

	}

	public void apply(Champion c) {
		// TODO Auto-generated method stub

		for (int i = 0; i < c.getAbilities().size(); i++) {
			Ability a = c.getAbilities().get(i);
			if (a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount() * 1.2));
			else if (a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount() * 1.2));
		}

	}

	@Override
	public void remove(Champion c) {

		for (int i = 0; i < c.getAbilities().size(); i++) {
			Ability a = c.getAbilities().get(i);
			if (a instanceof HealingAbility)
				((HealingAbility) a).setHealAmount((int) (((HealingAbility) a).getHealAmount() / 1.2));
			else if (a instanceof DamagingAbility)
				((DamagingAbility) a).setDamageAmount((int) (((DamagingAbility) a).getDamageAmount() / 1.2));
		}
	}

	@Override
	public Effect clone() throws CloneNotSupportedException{
		PowerUp temp = new PowerUp(getDuration());
		return temp;
	}

}
