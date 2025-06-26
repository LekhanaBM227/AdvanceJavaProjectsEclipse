package com.hibernate.onetomany;


public class App
{
    public static void main( String[] args )
    {
    	Publisher p = new Publisher();
    	p.setPid(1);
    	p.setPname("Penguine");
    	p.setEmail("pct@gmail.com");
    	p.setPhone(708867096);
    	
    	Book b1 = new Book();
    	b1.setBname("Karvalo");
    	b1.setAuthor("PCThejasvi");
    	b1.setCost(400);
    	b1.setRating(4);
    	b1.setPub(p);
    	
    	Book b2 = new Book();
    	b2.setBname("Marali mannige");
    	b2.setAuthor("shivaram");
    	b2.setCost(500);
    	b2.setRating(5);
    	b2.setPub(p);
    	
    	Book b3 = new Book();
    	b3.setBname("Malegalalli madhumagalu");
    	b3.setAuthor("Kuvempu");
    	b3.setCost(600);
    	b3.setRating(5);
    	b3.setPub(p);
    
    	HibernateManager1 hbm = new HibernateManager1();
    	hbm.insertData(b1);
    	hbm.insertData(b2);
    	hbm.insertData(b3);
    }
}
