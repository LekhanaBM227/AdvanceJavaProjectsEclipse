package com.hibernate.manytomany;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "Student_tbl")
public class Student {

	@Id
	int id;
	@Column
	String name;
	
	@ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "Student_Course", JoinColumns = {@JoinColumn(name = "id")}, inverseJoinColumns = {@JoinColumn(name="cid")})
	Set cou;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getCou() {
		return cou;
	}

	public void setCou(Set cou) {
		this.cou = cou;
	}
}
