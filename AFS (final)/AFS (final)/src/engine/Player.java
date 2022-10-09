package engine;

import java.util.ArrayList;

import javafx.scene.image.Image;
import model.world.Champion;

public class Player {
	private String name;
	private ArrayList<Champion> team;
	private Champion leader;
	private ArrayList<Image> Images;
	
	

	
	public Player(String name) {
		this.name = name;
		team = new ArrayList<Champion>();
		Images = new ArrayList<Image>();
	}


	public ArrayList<Image> getImages() {
		return Images;
	}


	public void setImages(ArrayList<Image> images) {
		Images = images;
	}


	public Champion getLeader() {
		return leader;
	}

	public void setLeader(Champion leader) {
		this.leader = leader;
	}
	
	public String displayPlayer (Champion curr)
	{
		String s = "Player Name: " + name + "\n" 
				+ "Leader: " + leader.getName() + "\n";
		for (Champion c: team)
		{
			if (c== curr)
				s += c.displayPlaying();
			else
				s += c.displayMe4Playing();
			s += "\n" +"\n";
		}
		return s;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Champion> getTeam() {
		return team;
	}


}
