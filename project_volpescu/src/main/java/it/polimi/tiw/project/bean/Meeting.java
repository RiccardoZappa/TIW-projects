package it.polimi.tiw.project.bean;

import java.sql.Time;
import java.util.Date;



public class Meeting {
	private int id_r;
	private String title;
	private Date start_date;
	private Time end_date ;
	private	int n_max_p = 30;
	private String us_creator;


	public int getId () {
		return id_r;
	}
	public void setId(int i) {
		this.id_r = i;
	}
	public String getTitle () {
		return title;
	}
	public void setTitle(String t) {
		this.title = t;
	}
	public Date getStart () {
		return start_date;
	}
	public void setStart(Date d) {
		this.start_date = d;
	}
	public int getNmax () {
		return n_max_p;
	}
	
	public String getUsCreator () {
		return us_creator;
	}
	public void setUsCreator(String i) {
		this.us_creator = i;
	}
	
	public Time getEnd() {
		return end_date;
	}
	public void setEnd(Time d) {
		this.end_date = d;
	}
	
}
