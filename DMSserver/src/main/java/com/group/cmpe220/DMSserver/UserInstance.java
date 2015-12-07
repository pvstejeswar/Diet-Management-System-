package com.group.cmpe220.DMSserver;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class UserInstance
{

	private volatile static UserInstance instance;
	private volatile static ConcurrentMap<String, Users> userlist;
	private static Cluster cluster;
	private static Session session;

	private UserInstance()
	{

	}

	//public void FillMap()
	//{
	//    Users u;
	//    ResultSet results = session.execute("SELECT * FROM users");
	//    for (Row row : results) {
	//        int id =row.getInt("id");
	//        String name = row.getString("name");
	//        int age = row.getInt("age");
	//        int weight = row.getInt("weight");
	//        int height = row.getInt("height");
	//        String allergies = row.getString("allergies");
	//        String gender = row.getString("gender");
	//        int calorie_limit = row.getInt("calorie_limit");
	//        int current_calorie = row.getInt("current_calorie");
	//        u = new Users(id,name,age,gender,weight,height,allergies,current_calorie,calorie_limit);
	//        userlist.putIfAbsent(name, u);
	//    }
	//
	//}

	public static synchronized UserInstance getInstance()
	{
		if(instance == null)
		{
			instance = new UserInstance();
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect("stg");
		}
		return instance;            
	}

	public Users registerUser(String name,String password,int id, int age,String gender,int height, int weight, String allergies)
	{
		Users temp = null;

		if(userlist == null)
			userlist = new ConcurrentHashMap<>();

		if(userlist.containsKey(name))
		{
			temp = userlist.get(name);
		}
		else
		{
			/* TODO check in database if present create class as per database values*/
			temp = new Users(id, name, age, gender, height, weight, allergies);
			/*TODO Save in database insert */ 
			String Query = "INSERT INTO users (name,age,gender,height,weight,calorie_limit,current_calorie,password,date) VALUES ('" + name + "'," + age + ",'" + gender + "'," + height +"," + weight + "," + temp.getCalorie_limit() + "," + temp.getCurrent_calorie() + "," + "'"+ password +"','" + "2015-12-05" + "')";
			userlist.putIfAbsent(name, temp);
			session.execute(Query);

		}

		return temp;    
	}

	public Users getFromDataBase(String Name)
	{
		Users temp = null;
		ResultSet resultset = session.execute("SELECT * FROM users WHERE name='" + Name + "';");
		Row row = resultset.one();	
		String name = row.getString("name");
		int age = row.getInt("age");
		int weight = row.getInt("weight");
		int height = row.getInt("height");
		//String allergies = row.getString("allergies");
		String gender = row.getString("gender");
		int calorie_limit = row.getInt("calorie_limit");
		int current_calorie = row.getInt("current_calorie");
		temp = new Users(7, name, age, gender, height, weight, "egg", current_calorie, calorie_limit);

		return temp;
	}

	public String FoodTake(String Name,int calories)
	{
		Users temp = null;
		if(userlist == null)
			userlist = new ConcurrentHashMap<>();
		
		if(userlist.containsKey(Name))
		{	
			temp = userlist.get(Name);
		}
		else
		{
			temp = getFromDataBase(Name);
			userlist.putIfAbsent(Name, temp);

		}
		boolean ret = temp.ConsumeFood(calories);   
		//String query = "UPDATE users SET current_calorie=" + temp.getCurrent_calorie() + " WHERE name = " + Name ;
		String query = "UPDATE users SET current_calorie=" + temp.getCurrent_calorie() + " WHERE name ='" + Name +"';";
		System.out.println(query);
		session.execute(query);
		return (ret == true)?"SAFE":"UNSAFE";
		/*TODO Update calorie count in database temp.getcurrentcalorie*/    
	}


	public void deregisterUser(String name)
	{
		if(userlist.containsKey(name))
		{
			userlist.remove(name);
			/*TODO remove from database */
		}

	}


	public Users getUser(String name)
	{
		Users temp = null;

		if(userlist.containsKey(name))
		{
			temp = userlist.get(name);
		}

		return temp;

	}

	public static void main(String[] args) {

		UserInstance ui = UserInstance.getInstance();
		ui.registerUser("xyz","abc", 6, 24, "male", 165, 170,"peanuts");

	}

}
