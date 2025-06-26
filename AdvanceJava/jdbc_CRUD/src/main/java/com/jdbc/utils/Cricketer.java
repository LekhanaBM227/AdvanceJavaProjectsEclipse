package com.jdbc.utils;

public class Cricketer {

	String name;
	String country;
	int age;
	int matches;
	int runs;
	int wickets;
	int catches;
	int mom;
	double average;

	public Cricketer(String name, String country, int age, int matches, int runs, int wickets, int catches,
			int mom, double average) {
		super();
		this.name = name;
		this.country = country;
		this.age = age;
		this.matches = matches;
		this.runs = runs;
		this.wickets = wickets;
		this.catches = catches;
		this.mom = mom;
		this.average = average;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public int getCatches() {
		return catches;
	}

	public void setCatches(int catches) {
		this.catches = catches;
	}

	public int getMom() {
		return mom;
	}

	public void setMom(int mom) {
		this.mom = mom;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}







}
