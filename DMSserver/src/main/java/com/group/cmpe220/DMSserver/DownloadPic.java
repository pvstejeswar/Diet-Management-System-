package com.group.cmpe220.DMSserver;

/**
 * @author Crunchify.com
 * 
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.Base64;
import android.util.Base64;
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.apache.commons.codec.binary.Base64;
 
@Path("/")
public class DownloadPic {
	@POST
	@Path("/{name}/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	
	public void download(InputStream incomingData, @PathParam("name") String member_name) throws IOException {
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Backend obj = new Backend(member_name, incomingData);
		
		obj.upload_image();
//		System.out.println(temp.getCalories());
//		return temp;
//         
	}
	
	 
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "RESTService Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
	 
}
