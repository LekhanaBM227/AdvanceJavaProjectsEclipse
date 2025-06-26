package com.hibernate.manytomany;

import java.util.HashSet;
import java.util.Set;

public class App 
{
    public static void main( String[] args )
    {
    	Course c1 = new Course();
    	c1.setId(1);
    	c1.setCname("java");
    	
    	Course c2 = new Course();
    	c2.setId(2);
    	c2.setCname("SQL");
    	
    	Set<Course> s1 = new HashSet<Course>();
    	s1.add(c1);
    	s1.add(c2);
    	
    	Student st1 = new Student();
    	st1.setId(1);
    	st1.setName("Deepak");
    	st1.setCou(s1);
    	
    	Student st2 = new Student();
    	st2.setId(1);
    	st2.setName("Deepak");
    	st2.setCou(s1);
    	
    	HibernateManager2 hbm = new HibernateManager2();
    	hbm.insertData(st1);
    	hbm.insertData(st2);
    }
}
