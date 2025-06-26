package com.SpringDependency1;

import org.springframework.context.annotation.Bean;

public class AppConfig {
    @Bean
    public Employee employee() {
    	Employee e=new Employee();
    	e.setAge(21);
    	e.setEmail("lee@gmail.com");
    	e.setName("leena");
    	e.setPhone(456789);
    	return e;
    }
}
