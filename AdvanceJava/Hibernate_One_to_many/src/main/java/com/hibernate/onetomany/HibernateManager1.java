package com.hibernate.onetomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateManager1 {
	private static Session session;
	public HibernateManager1() {
		StandardServiceRegistry registry=new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		SessionFactory sessionFactory=new MetadataSources(registry).buildMetadata().buildSessionFactory();
		session=sessionFactory.openSession();
		System.out.println("Connection is established");
	}
	
	public void insertData(Book b)
	{
		org.hibernate.Transaction beginTransaction = session.beginTransaction();
		session.save(b);
		beginTransaction.commit();
		System.out.println("Data is added.");
		session.evict(b);
	}
}
