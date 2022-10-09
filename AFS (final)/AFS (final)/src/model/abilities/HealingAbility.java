package model.abilities;

import java.util.ArrayList;

import model.world.*;

public class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			int healingAmount) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	public void execute(ArrayList<Damageable> targets) {
		for (int i = 0; i < targets.size(); i++) {
			Champion cur = (Champion) targets.get(i);
			cur.setCurrentHP(Math.min(cur.getMaxHP(), cur.getCurrentHP() + healAmount ));
		}
		setCurrentCooldown(getBaseCooldown());
	}
	
	public String toString1 ()
	{
		return "\n"+"Ability " + getName() + ": \n Type: Healing \n Area of Effect " + getCastArea()
				+ "\n Cast Range: " + getCastRange() + "\n Mana cost: " + getManaCost();
	}
	
	public String toString2 ()
	{
		return "\n AP cost: " + getRequiredActionPoints() + "\n Base Cooldown: " + getBaseCooldown() + 
				"\n Current Cooldown: " + getCurrentCooldown()
				+ "\n Heal Amount: " + getHealAmount()+ "\n";
	}


}
