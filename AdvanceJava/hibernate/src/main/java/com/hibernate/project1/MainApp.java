package com.hibernate.project1;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
public class MainApp {
	private static Session session;
	public static void main(String args[]) {
		Student s=new Student();
		s.setId(1);
		s.setName("Anvi");
		
		StandardServiceRegistry registry=new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		SessionFactory sessionFactory=new MetadataSources(registry).buildMetadata().buildSessionFactory();
		session=sessionFactory.openSession();
		System.out.println("Connection is established");
		Transaction beginTransaction=session.beginTransaction();
//		session.save(s);
//		System.out.println("Data is stored on the DB table");
//		beginTransaction.commit();
//		
//		Query  query=session.createQuery("From Student");
//		List list=query.list();
//		Iterator iterator=list.iterator();
//		while(iterator.hasNext()) {
//			Student s1=(Student)iterator.next();
//			System.out.println(s1.getId()+"------------"+s1.getName());
//			System.out.println("=====================");
//		}
//		beginTransaction.commit();
		
		
		
//		Student s1=(Student)session.get(Student.class,1);
//		s1.setName("shalini");
//		session.update(s1);
//		System.out.println("Data is updated");
//		beginTransaction.commit();
	//	
		Student s1=(Student)session.get(Student.class, 1);
		session.delete(s1);
		System.out.println("Data is deleted");
		beginTransaction.commit();
	}
}
