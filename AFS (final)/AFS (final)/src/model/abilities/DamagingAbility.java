package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	public void execute(ArrayList<Damageable> targets) {
		for(int i=0;i<targets.size();i++) {
			Damageable curr = targets.get(i);
			
			if (curr instanceof Champion && ((Champion) curr).checkEffect("Shield"))
			{
				int index = ((Champion) curr).findEffect("Shield");
				((Champion) curr).getAppliedEffects().remove(index);
			}
			else {
				curr.setCurrentHP(Math.max(0, curr.getCurrentHP()-damageAmount));
			}
			
		}
		setCurrentCooldown(getBaseCooldown());
	}
	
	public String toString1 ()
	{
		return "\n"+ "Ability " + getName() + ":\n Type: Damaging \n Area of Effect " + getCastArea()
				+ "\n Cast Range: " + getCastRange() + "\n Mana cost: " + getManaCost();
	}
	
	public String toString2 ()
	{
		return  "\n AP cost: " + getRequiredActionPoints() + "\n Base Cooldown: " + getBaseCooldown() + 
				"\n Current Cooldown: " + getCurrentCooldown()
				+ "\n Damage Amount: " + damageAmount + "\n";
	}
	

}
