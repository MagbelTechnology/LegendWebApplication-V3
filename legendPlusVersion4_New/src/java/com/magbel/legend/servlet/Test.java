package com.magbel.legend.servlet;

import org.json.JSONArray;
import org.json.JSONObject;

import ng.com.magbel.token.ZenithTokenClass;

public class Test {

	public static void main(String[] args) {
		   try {
		String batchNo = "1243";
		String status = ZenithTokenClass.batchStatusValidation(batchNo);
		   JSONObject json = new JSONObject(status);
		   JSONArray jArray = json.getJSONArray("data");
		 // System.out.println(json.toString());
		  
		  String responseCode =  json.getString("responseCode");
		  String responseDescription = json.getString("responseDescription");
		  
		  System.out.println("Response Code: " + responseCode);
		  System.out.println("Response Description: " + responseDescription);
		  System.out.println("\n");
//		  System.out.println(jArray.toString());
	  System.out.println(" Batch Details: ");
		  for(int i=0; i<jArray.length(); i++) {
			 // System.out.println("Array Length:" + jArray.length());
			  JSONObject json2 = jArray.getJSONObject(i);
			  String reponseStatus = json2.getString("status");
			  String statusDesc = json2.getString("statusDesc");
			  JSONObject json3 = json2.getJSONObject("batch");
			  String totalCount = String.valueOf(json3.getInt("totalCount"));
			  String processed = String.valueOf(json3.getInt("processed"));
			  String pending = String.valueOf(json3.getInt("pending"));
			  String failed = String.valueOf(json3.getInt("failed"));
			  String suspense = String.valueOf(json3.getInt("suspense"));
			  
			 // System.out.println("json3:" + json3);
			  System.out.println("totalCount:" + totalCount);
			  System.out.println("processed:" + processed);
			  System.out.println("pending:" + pending);
			  System.out.println("failed:" + failed);
			  System.out.println("suspense:" + suspense);
			  System.out.println("reponseStatus:" + reponseStatus);
			  //System.out.println("Processed: " + processed);

	}
	}catch(Exception e) {
		e.getMessage();
	}
	}
}
