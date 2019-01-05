package org.att.preTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ParsingJSON {
	public static final String httpURLParam = "http-URL"; //property file parameter name
	public static final String NUMBERS = "numbers";
	public static String propFileName = "config.properties";

	public static void main(String[] args) {
		String httpURLGet = null;
		
		httpURLGet = getHttpURL();//user defined function which returns httpURL
		if(httpURLGet == null) {
			System.out.println("Please specify the value for the param '"+httpURLParam+"' in the properties file");
			return;
		}
		
		JSONArray response = readJSONFromURL(httpURLGet);
		
		parseJSON(response);
		
	}
	
	//function to locate properties files and reading param value
	public static String getHttpURL(){
		Properties prop = new Properties();
		InputStream inputStream = null;
		String url = null;
		try {
			inputStream = ParsingJSON.class.getClassLoader().getResourceAsStream(propFileName);
			
			if(inputStream != null) {
				prop.load(inputStream);
			}
			else {
				throw new FileNotFoundException("properties file '"+propFileName+"'not found");
			}
			url = prop.getProperty(httpURLParam).trim();
			return url;
		}catch(IOException io) {
			System.out.println("Not able to retrieve the value for the param '"+httpURLParam+"'");
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return url;
	}
	
	//function to access http get request to get JSON response
	public static JSONArray readJSONFromURL(String httpURLGet) {
	    StringBuilder sb = new StringBuilder();
	    JSONArray JSONData = new JSONArray();
	    try {
	        URL url = new URL(httpURLGet);
	        BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));
	        String st;
			while ( (st = rd.readLine().toString()) != null) {
				sb.append(st);
			}
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(sb.toString());
			JSONData.add(json);
	    }catch (ParseException e) {
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println("Unable to process the URL "+e.getMessage());
		}
        return JSONData;
	}
	
	//function to process the JSON object
	public static void parseJSON(JSONArray data) {
		try {
		  int grandTotal = 0;
		  for (Object o : data) 
		  {
		    JSONObject obj = (JSONObject) o;
		    JSONArray numArray = (JSONArray) obj.get(NUMBERS);

		    int arraySum = 0;
	        Iterator nums = numArray.iterator(); 
	        while (nums.hasNext())  
	        { 
	        	arraySum += (Integer) nums.next();
	        }
		    grandTotal += arraySum;
		  }
		  System.out.println("sum of number array in a given JSON file is :"+grandTotal);
		}
		catch(Exception e) {
			System.out.println("unable to parse a given JSON :"+e.getMessage());
		}
	}
	

}
