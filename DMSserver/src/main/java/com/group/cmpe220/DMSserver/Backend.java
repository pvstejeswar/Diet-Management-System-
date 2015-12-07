package com.group.cmpe220.DMSserver;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fatsecret.platform.FatSecretAPI;

import android.util.Base64;

public class Backend {
	static String name;
	static String image_path = "C:\\Users\\Tejeswar PVS\\Desktop\\Cmpe_220\\pic.jpg";
	static InputStream incomingData;
	static String url_path = "http://res.cloudinary.com/dl6fi0f2o/image/upload/sample_remote.jpg";
	public Backend(String name, InputStream incomingData)
	{
		this.name = name;
		this.incomingData = incomingData;
	}
	public Backend(String name)
	{
		this.name = name;

	}
	public static void upload_image() throws IOException
	{
		Path path = Paths.get(image_path);
		try {
			System.out.println("=== Deleting Image ====");
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File doesnot exist");
		}

		String local_path = download_image(incomingData);

		if (local_path.equals("0"))
		{
			System.out.println("Unable to retrieve an image");
		}
	}


	public static Generator execute() throws IOException
	{
		//		Path path = Paths.get(image_path);
		//		try {
		//			Files.deleteIfExists(path);
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			System.out.println("File doesnot exist");
		//		}
		//		
		//		String local_path = download_image(incomingData);
		//		
		//		if (local_path.equals("0"))
		//		{
		//			System.out.println("Unable to retrieve an image");
		//		}
		for(int i = 0; i< 10; i++)
		{
			File f = new File(image_path);
			if(f.exists() && !f.isDirectory()) { 
				break;
			}
			else
			{
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		String product_name = SendPath.IdentifyFood(image_path);
		if (product_name.equals("not found"))
		{
			System.out.println("We dont have image for this product");
		}
		else
		{
			Generator g = restCall(product_name);
			if (g == null)
			{
				System.out.println("Product is not present in Fatsecret server");
			}
			else
			{
				String temp = g.getCalories().substring(0,g.getCalories().indexOf("kcal"));
				int calories = Integer.parseInt(temp);
				String final_result = UserInstance.getInstance().FoodTake(name, calories);
				g.setFinal_result(final_result);
				return g;
			}
		}


		return null;




	}




	//	public static String download_image(String url)
	//	{
	//		try
	//		{
	//		String path = "C:\\Users\\Tejeswar PVS\\Desktop\\Cmpe_220\\pic.jpg";
	//		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
	//				 "cloud_name", "dl6fi0f2o",
	//				 "api_key", "172322623958866",
	//				 "api_secret", "Z-GZmxmje-eAqW1BLeuglpv9AeU"));
	//		
	//		
	//
	//		//System.out.println(cloudinary.url().generate("sample_remote.jpg"));
	//		//URL url1 = new URL(url);
	//		//BufferedImage image = ImageIO.read(url1);
	//		
	//		//ImageIO.write(image, "jpg",new File(path));
	//		String temp = cloudinary.url().generate("sample_remote.jpg");
	//		try {
	//			Thread.sleep(15000);
	//		} catch (InterruptedException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}		
	//		
	//		 URL weburl = new URL(temp);
	//	        InputStream in = new BufferedInputStream(weburl.openStream());
	//	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	//	        byte[] buf = new byte[1024];
	//	        int n = 0;
	//	        while (-1!=(n=in.read(buf)))
	//	        {
	//	            out.write(buf, 0, n);
	//	        }
	//	        out.close();
	//	        in.close();
	//	        byte[] response = out.toByteArray();
	//	        FileOutputStream fos = new FileOutputStream("C:\\Users\\Tejeswar PVS\\Desktop\\Cmpe_220\\pic.jpg");
	//	        fos.write(response);
	//	        fos.close();
	//		
	//		
	//		return path;
	//		}
	//		catch(Exception e)
	//		{
	//			return "0";
	//		}
	//	}

	public static String download_image(InputStream incoming_data) throws IOException
	{
		StringBuilder sample_string = new StringBuilder();
		try
		{

			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				sample_string.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + sample_string.toString());





		// return HTTP response 200 in case of success
		//return Response.status(200).entity(crunchifyBuilder.toString()).build();

		String pic = sample_string.toString();

		pic = pic.substring(pic.indexOf(":\"")+2);
		pic = pic.substring(0,pic.length()-2);

		pic = pic.replace("\\n", "");


		//	BASE64Decoder decoder = new BASE64Decoder();
		//	byte[] imageByteArray = decoder.decodeBuffer(pic);
		// byte[] imageByteArray = decode(pic);
		byte[] imageByteArray = Base64.decode(pic,Base64.DEFAULT);
		//byte[] imageByteArray = Base64.decodeBase64(pic);
		// String text = new String(imageByteArray, "UTF-8");
		// Write a image byte array into file system

		FileOutputStream imageOutFile = new FileOutputStream(
				image_path);
		//byte[] dontknow = text.getBytes("UTF-8");
		//System.out.println(text);
		//System.out.println("32");
		imageOutFile.write(imageByteArray);
		imageOutFile.close();
		return image_path;
	}
	public static Generator restCall(String product_name)
	{
		try
		{
			FatSecretAPI api = new FatSecretAPI("99bd06bec7b740918c06922d66b45df6", "58f0242bc3b548199b512f41cd05e315");
			//		//For getting food items
			Service ser_obj = new Service();
			JSONObject foods = api.getFoodItems(product_name);
			//System.out.println(getFood(foods));
			JSONArray a = (JSONArray)getFood(foods);
			JSONObject acd = (JSONObject) a.get(0);
			Generator g = new Generator();
			ser_obj.getall(acd, g);
			return g;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static Object getFood(JSONObject food)
	{
		Iterator<String> keys = food.keys();
		if( keys.hasNext() ){
			String key = (String)keys.next(); // First key in your json object
			System.out.println(key);
			if (key.equals("food") || key.equals("page_number"))
			{
				return food.get("food");
			}
			else
			{
				return getFood((JSONObject) food.get(key));
			}
		}
		else
		{
			return null;
		}
	}




}
