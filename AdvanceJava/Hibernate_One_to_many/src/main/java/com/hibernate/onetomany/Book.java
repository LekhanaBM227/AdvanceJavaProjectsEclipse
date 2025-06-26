package com.hibernate.onetomany;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Book", schema = "hib_gqt_db")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int bid;
	
	@Column
	String bname;
	
	@Column
	String author;
	
	@Column
	int cost;
	
	@Column
	int rating;
	
	@ManyToOne(targetEntity = Publisher.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "pub_pid", referencedColumnName = "pid")
	Publisher pub;
	
	
	
	
	public Publisher getPub() {
		return pub;
	}
	public void setPub(Publisher pub) {
		this.pub = pub;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
