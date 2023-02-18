package it.polimi.tiw.project.bean;

import java.util.List;

public class Partecipants {
	private List<String> username_i;
	private int id_meeting;
	
	public List<String> getUsername_i(){
		return username_i;
	}
	public void SetList (List<String> d) {
		this.username_i = d;
	}
	

	public int getId_meeting() {
		return id_meeting;
	}
	public void SetIdMeeting(int id) {
		this.id_meeting = id;
	}
	

}
