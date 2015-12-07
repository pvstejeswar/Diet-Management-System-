package com.group.cmpe220.DMSserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendPath {

	public static String IdentifyFood(String path)
	{
		try
		{
		System.out.println("======= Connecting to socket ========\n");
		System.out.println("======= Path is " + path + "========\n");
		Socket socket = new Socket("localhost", 12345);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//String path = "C:\\Atomiton\\workspace\\ImageSender\\src\\main\\java\\apple_dest.png";
		//String path = "C:\\Atomiton\\workspace\\ImageSender\\src\\main\\java\\lays.gif";
		out.flush();
		out.println(path);
		String Item = br.readLine();
		System.out.println("Item is " + Item);
		socket.close();
		return Item;
		}
		catch(Exception e)
		{
			return "not found";
		}
	}
	
	public static void main(String args[])
	{
		String path = "C:\\Users\\Tejeswar PVS\\Desktop\\Cmpe_220\\pic.jpg";
		//String path = "C:\\Users\\Tejeswar PVS\\Desktop\\Cmpe_220\\Lays.png";
		IdentifyFood(path);
		
	}

}
