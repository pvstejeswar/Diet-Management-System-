package com.group.cmpe220.DMSserver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;



public class Service {
	
	public Generator getall(JSONObject obj, Generator g)
	{
//		Generator g = new Generator(1,"ishan","number10");
//		Generator g2 = new Generator(1,"arora","number11");
//		List<Generator> l = new ArrayList<Generator>();
//		l.add(g);
//		l.add(g2);
		//Generator g = new Generator();
		g.setFood_name(obj.getString("food_name"));
		g.setFood_type(obj.getString("food_type"));
		g.setNutrient_ref(obj.getString("food_url"));
		String calories = obj.getString("food_description").substring(obj.getString("food_description").indexOf("Calories"));
		
		String fat = obj.getString("food_description").substring(obj.getString("food_description").indexOf("Fat"));
		String carbs = obj.getString("food_description").substring(obj.getString("food_description").indexOf("Carbs"));
		String protein = obj.getString("food_description").substring(obj.getString("food_description").indexOf("Protein"));
		String ref_size = obj.getString("food_description");
//		System.out.println(calories);
//		System.out.println(carbs);
//		System.out.println(protein);
//		System.out.println(fat);
		
		g.setCalories(calories.substring(calories.indexOf("Calories")+10, calories.indexOf("|")-1));
		g.setFat(fat.substring(fat.indexOf("Fat")+5, fat.indexOf("|")-1));
		g.setCarbs(carbs.substring(carbs.indexOf("Carbs")+7, carbs.indexOf("|")-1));
		g.setProtein(protein.substring(protein.indexOf("Protein")+10, protein.indexOf("g")+1));
		g.setRef_size(ref_size.substring(ref_size.indexOf("Per"),ref_size.indexOf(" - ")));
//		System.out.println(g.getCalories());
//		System.out.println(g.getFat());
//		System.out.println(g.getCarbs());
//		System.out.println(g.getProtein());
//		System.out.println(g.getRef_size());
		
		
		
		
		return g;
		
	}

}
