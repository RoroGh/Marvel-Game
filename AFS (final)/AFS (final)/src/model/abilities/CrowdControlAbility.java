package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException{
		for (int i = 0; i < targets.size(); i++) {
			Effect effect2 = (Effect) effect.clone();
			((Champion) targets.get(i)).getAppliedEffects().add(effect2);
			effect2.apply((Champion) targets.get(i));
		}
		
		setCurrentCooldown(getBaseCooldown());
	}

	public Effect getEffect() {
		return effect;
	}
	
	public String toString1 ()
	{
		return "\n"+ "Ability " + getName() + ":\n Type: Crowd Control \n Area of Effect " + getCastArea()
				+ "\n Cast Range: " + getCastRange() + "\n Mana cost: " + getManaCost();
	}
	
	public String toString2 ()
	{
		return  "\n AP cost: " + getRequiredActionPoints() + "\n Base Cooldown: " + getBaseCooldown() + 
				"\n Current Cooldown: " + getCurrentCooldown()
				+ "\n Effect: " + effect + "\n";
	}

}
