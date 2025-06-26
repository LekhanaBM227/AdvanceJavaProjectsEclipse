package com.hibernate.onetoone;

public class App 
{
    public static void main( String[] args )
    {
        Address a = new Address();
        a.setAddline("13th cross sarakki");
        a.setCity("Banglore");
        a.setState("KAR");
        a.setCountry("IND");
        a.setPincode(576991);
        
        Users u = new Users();
        u.setName("Shyam");
        u.setAdd(a);
        
        HibernateManager hbm = new HibernateManager();
        hbm.insertData(u);
    }
}
