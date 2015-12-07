package com.group.cmpe220.DMSserver;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Generator {
	
	
	String food_type;
	String nutrient_ref;
	String food_name;
	String Calories;
	String fat;
	String Carbs;
	String Protein;
	String ref_size;
	String final_result;
	
	
	public String getFinal_result() {
		return final_result;
	}


	public void setFinal_result(String final_result) {
		this.final_result = final_result;
	}


	public Generator() {
		// TODO Auto-generated constructor stub
	}


	public String getFood_type() {
		return food_type;
	}


	public void setFood_type(String food_type) {
		this.food_type = food_type;
	}


	public String getNutrient_ref() {
		return nutrient_ref;
	}


	public void setNutrient_ref(String nutrient_ref) {
		this.nutrient_ref = nutrient_ref;
	}


	public String getFood_name() {
		return food_name;
	}


	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}


	public String getCalories() {
		return Calories;
	}


	public void setCalories(String calories) {
		Calories = calories;
	}


	public String getFat() {
		return fat;
	}


	public void setFat(String fat) {
		this.fat = fat;
	}


	public String getCarbs() {
		return Carbs;
	}


	public void setCarbs(String carbs) {
		Carbs = carbs;
	}


	public String getProtein() {
		return Protein;
	}


	public void setProtein(String protein) {
		Protein = protein;
	}


	public String getRef_size() {
		return ref_size;
	}


	public void setRef_size(String ref_size) {
		this.ref_size = ref_size;
	}
	
	



	
}
