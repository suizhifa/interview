package com.lzkj.file.interview.entity;

import java.io.Serializable;
import java.util.Date;

public class StartDateInfo implements Serializable {
	private static final long serialVersionUID = 8325882509007088323L;
	private int id;
	private int type;
	private Date startDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	

}
