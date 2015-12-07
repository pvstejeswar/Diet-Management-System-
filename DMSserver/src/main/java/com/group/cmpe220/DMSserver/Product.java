package com.group.cmpe220.DMSserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fatsecret.platform.FatSecretAPI;
//import com.sun.jersey.core.header.FormDataContentDisposition;

@Path("/products")
public class Product {
	
	//Service s = new Service();
	@GET
	@Produces(MediaType.APPLICATION_JSON)
//	public List<Generator> getMessage()
//	{
//		//return s.getall();
//	}
	public String getMessage()
	{
		return "Successful";
		
	}
	
	
	
	
	@GET
	@Path("/{name}")
	@Produces("application/json")
	public Generator single(@PathParam("name") String member_name) throws IOException
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Backend obj = new Backend(member_name);
//		FatSecretAPI api = new FatSecretAPI("99bd06bec7b740918c06922d66b45df6", "58f0242bc3b548199b512f41cd05e315");
//		//For getting food items
//		Service ser_obj = new Service();
//		JSONObject foods = api.getFoodItems("apple");
//		System.out.println(foods);
//		
//		//System.out.println(getFood(foods));
//		
//		JSONArray a = (JSONArray)getFood(foods);
//		System.out.println("ishan");
//		JSONObject acd = (JSONObject) a.get(0);
		
		
		
		//System.out.println(a.get(0));
				
		return obj.execute();
	}
	
	
	
	
	
}
