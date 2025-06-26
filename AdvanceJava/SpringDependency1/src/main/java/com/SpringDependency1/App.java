package com.SpringDependency1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App 
{
    public static void main( String[] args )
    {
//    	ApplicationContext ac = new  AnnotationConfigApplicationContext(AppConfig.class);
        ApplicationContext ac =new ClassPathXmlApplicationContext("applicationContext.xml");
        Employee e=ac.getBean(Employee.class);
        System.out.println(e.getName());
        System.out.println(e.getEmail());
        System.out.println(e.getAge());
        System.out.println(e.getPhone());
        
    }
}
