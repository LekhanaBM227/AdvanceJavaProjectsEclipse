package com.hibernate.onetoone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateManager {
	private static Session session;
	public HibernateManager() {
		StandardServiceRegistry registry=new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		SessionFactory sessionFactory=new MetadataSources(registry).buildMetadata().buildSessionFactory();
		session=sessionFactory.openSession();
		System.out.println("Connection is established");
	}
	
	public void insertData(Users u)
	{
		org.hibernate.Transaction beginTransaction = session.beginTransaction();
		session.save(u);
		System.out.println("Data is added.");
		beginTransaction.commit();
	}
}
