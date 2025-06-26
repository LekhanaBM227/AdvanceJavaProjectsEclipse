package com.hibernate.project1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="Student", schema="hib_gqt_db")
public class Student {
	@Id
	int id;
	@Column
	String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
	this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}

}