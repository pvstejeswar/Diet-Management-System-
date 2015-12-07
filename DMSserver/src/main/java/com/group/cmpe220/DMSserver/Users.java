package com.group.cmpe220.DMSserver;

public class Users {

private int Id;
private String Name;
private int Age;
private String Gender;
private int Height;
private int Weight;
private String Allergies;
private int Calorie_limit;
private int Current_calorie;

public Users() {}

public Users(int id, String name, int age,String gender,int height, int weight, String allergies){
    this.Id=id;
    this.Name=name;
    this.Age=age;
    this.Gender=gender;
    this.Height=height;
    this.Weight=weight;
    this.Allergies=allergies;
    Current_calorie = 0;
    CalcCalorieLimit();

}

public Users(int id, String name, int age,String gender,int height, int weight, String allergies,int calorieCount,int calorieLimit){
    this.Id=id;
    this.Name=name;
    this.Age=age;
    this.Gender=gender;
    this.Height=height;
    this.Weight=weight;
    this.Allergies=allergies;
    Current_calorie = calorieCount;
    Calorie_limit = calorieLimit;

}

public int getId(){return this.Id;}
public String getName(){return this.Name;}
public int getAge(){return this.Age;}
public String getGender() {return this.Gender;}
public int getHeight() {return this.Height;}
public int getWeight() {return this.Weight;}
public String getAllergies() {return this.Allergies;}
public int getCalorie_limit() {return this.Calorie_limit;}
public int getCurrent_calorie() {return this.Current_calorie;}

public boolean ConsumeFood(int calories)
{
    Current_calorie += calories;

    return (Current_calorie < Calorie_limit)? true:false;

}

private void CalcCalorieLimit()
{
    switch(Gender.toLowerCase())
    {
    case "male":
        if(Age >= 2 && Age < 4)
            Calorie_limit = 1400;
        else if(Age >= 4 && Age < 9)
            Calorie_limit = 1600;
        else if(Age >= 9 && Age < 14)
            Calorie_limit = 2200;
        else if(Age >= 14 && Age < 19)
            Calorie_limit = 2600;
        else if(Age >= 19 && Age < 31)
            Calorie_limit = 2800;
        else if(Age >= 31 && Age < 51)
            Calorie_limit = 2600;
        else
            Calorie_limit = 2400;
        break;

    case "female":
        if(Age >= 2 && Age < 4)
            Calorie_limit = 1400;
        else if(Age >= 4 && Age < 9)
            Calorie_limit = 1600;
        else if(Age >= 9 && Age < 14)
            Calorie_limit = 2000;
        else if(Age >= 14 && Age < 19)
            Calorie_limit = 2000;
        else if(Age >= 19 && Age < 31)
            Calorie_limit = 2200;
        else if(Age >= 31 && Age < 51)
            Calorie_limit = 2000;
        else
            Calorie_limit = 1800;
        break;

    }

}


}
