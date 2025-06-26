package com.jdbc.project1;

import java.util.Scanner;

import com.jdbc.models.Cricketer_Model;
import com.jdbc.utils.Cricketer;

public class AppCrick {

	public static void main(String[] args) {

		Cricketer_Model cm = new Cricketer_Model();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Select the options:"
				+ "1.Insert the data."
				+ "0.Exit.");
		
		int ch = sc.nextInt();
		
		switch (ch) {
		case 1:
			System.out.print("Enter the name: ");
			String name = sc.next();
			System.out.print("Enter the country: ");
			String country = sc.next();
			System.out.print("Enter the age: ");
			int age = sc.nextInt();
			System.out.print("Enter the matches: ");
			int matches = sc.nextInt();
			System.out.print("Enter the runs: ");
			int runs = sc.nextInt();
			System.out.print("Enter the wickets: ");
			int wickets = sc.nextInt();
			System.out.print("Enter the catches: ");
			int catches = sc.nextInt();
			System.out.print("Enter the mom: ");
			int mom = sc.nextInt();
			System.out.print("Enter the average: ");
			double average = sc.nextDouble();
			
			Cricketer c = new Cricketer(name, country, age, matches, runs, wickets, catches, mom, average);
			boolean res = cm.insertData(c);
			
			if(res==true) {
				System.out.println("Data inserted!");
				System.out.println("Press Y...to...continue or N...to...end");
				if(sc.next().equalsIgnoreCase("y"))
				{
					main(null);
				}
				else
				{
					System.exit(0);
				}
			}
			
			break;
			
		case 0:
			System.exit(0);
			break;

		default:
			break;
		}
	}

}
