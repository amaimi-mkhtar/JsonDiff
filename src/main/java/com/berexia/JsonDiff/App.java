package com.berexia.JsonDiff;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;

import org.apache.commons.lang3.ClassUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Hello world! *
 */
public class App 
{
    public static void main( String[] args )
    {
    	JSONParser parser = new JSONParser();
    	 
        try {
 
            Object oldObj = parser.parse(new FileReader( "C:\\Users\\XPS\\Downloads\\oldJson.json"));
            JSONObject oldActorect = (JSONObject)  oldObj;
            
            Object newObj = parser.parse(new FileReader( "C:\\Users\\XPS\\Downloads\\newJson.json"));
            JSONObject newActorect = (JSONObject) newObj;
             compare(newActorect, oldActorect, "");
      

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //function compare that recieve two jsonObject and print Out the difference values/propreties of them
    
    public static void compare(JSONObject newActor,JSONObject oldActor,String parentProprety) throws IOException {
    	
       	Set<String> newActorKeySet = newActor.keySet();
       	Set<String> oldActorKeySet = oldActor.keySet();
    	
    	
 
    	// deleted proprerties
    	
    	 for (Iterator<String> oldkeys =  oldActor.keySet().iterator(); oldkeys.hasNext();) {
    		 String oldKey = oldkeys.next();
    		 if(!newActorKeySet.contains(oldKey)) {
					System.out.println(parentProprety+oldActor.get("id")+"."+oldKey+" : "+oldActor.get(oldKey)+" proprety deleted ");
					oldkeys.remove();
				}
    	 }
    	 
    	// added proprerties
    	 for (Iterator<String> newkeys = newActor.keySet().iterator(); newkeys.hasNext();) {
    		 String newKey = newkeys.next();
    		 if(!oldActorKeySet.contains(newKey)) {
					System.out.println(parentProprety+"."+newKey+":"+newActor.get(newKey).toString()+" proprety added ");
					newkeys.remove();
				}
    	 }
    	 
    		
    		
    		
    	//values changes
    	 
    	  	for (Object oldKey : oldActorKeySet) {
    	  			//primitive proprrety 
   	       			if(ClassUtils.isPrimitiveOrWrapper((Class<?>) oldActor.get(oldKey).getClass()) || oldActor.get(oldKey).getClass().equals(String.class)) {
	       				if(!newActor.get(oldKey).equals(oldActor.get(oldKey))) {
	       					System.out.println(parentProprety+"."+oldKey+" Updated "+oldActor.get(oldKey)+" --> "+newActor.get(oldKey));
	       				}
	    			}
   	       			
   	       			// jsonObject Proprety 
   	       			else if(oldActor.get(oldKey).getClass().equals(JSONObject.class)) {
							if(!newActor.get(oldKey).equals(oldActor.get(oldKey))) {
								compare( (JSONObject) oldActor.get(oldKey) ,(JSONObject) newActor.get(oldKey),parentProprety+"."+(String) oldKey);
							}
					}
   	       			// JsonArray proprety
   	       			else if(oldActor.get(oldKey).getClass().equals(JSONArray.class)) {
   	       				
						//if  the JsonArray Changed
						if(!newActor.get(oldKey).equals(oldActor.get(oldKey))) {
							JSONArray oldobjarray = (JSONArray)oldActor.get(oldKey);
							Object oldobj0 = oldobjarray.get(0);
							
							//  array nested prinmitive or string proprety
							if(ClassUtils.isPrimitiveOrWrapper((Class<?>) oldobj0.getClass()) || oldobj0.getClass().equals(String.class)) {
								for(Object oldobj:(JSONArray)oldActor.get(oldKey) ) {
									ArrayList newObj = (ArrayList) newActor.get(oldKey);
				   	       			if(!newObj.contains(oldobj)) {
				   	       				System.out.println(parentProprety+"."+oldKey+"[] value:"+oldobj+" deleted ");
				   	       				}
								}for(Object newobj:(JSONArray)newActor.get(oldKey) ) {
									ArrayList oldObj = (ArrayList) oldActor.get(oldKey);
				   	       			if(!oldObj.contains(newobj)) {
				   	       				System.out.println(parentProprety+"."+oldKey+"[] value:"+newobj+" added ");
				   	       				}
								}
			   	       			
			   	       		}
							// array nested Jsonobject  proprety 
							else if(oldobj0.getClass().equals(JSONObject.class)) {
								
				   	 				//if the  nested jsonObject has an id proprety 
					   	       		if(((JSONObject)oldobj0).keySet().contains("id")) {
					   	       			ArrayList<Long> oldNestedObjectIds = new ArrayList<Long>();
					   	       			ArrayList<Long> newNestedObjectIds = new ArrayList<Long>(); 
					   	       			for(Object oldobj:(JSONArray)oldActor.get(oldKey) ) {
					   	       				 JSONObject oldNestedObj = (JSONObject) oldobj;
							   	       		  
							 	             oldNestedObjectIds.add((Long)  oldNestedObj .get("id"));
						 	             
							 	         for(Object newobj:(JSONArray)newActor.get(oldKey) ) {
							 	        	JSONObject newNestedObj = (JSONObject) newobj;
							 	        	if(!newNestedObjectIds.contains((Long)  newNestedObj.get("id"))) {newNestedObjectIds.add((Long)  newNestedObj.get("id"));}
							 	        	// two array nested jsonObject with the same id
							 	        	if(oldNestedObj.get("id").equals(newNestedObj.get("id"))) {
							 	        		compare(newNestedObj,oldNestedObj,parentProprety+"."+oldKey+"[]."+newNestedObj.get("id"));
							 	        	}
							 	         }
							 	       }
					   	       			//deleted nested objects
					   	       			//System.out.println(oldNestedObjectIds);
					   	       			for (Long oldNestedObjectId : oldNestedObjectIds) {
											if(!newNestedObjectIds.contains(oldNestedObjectId)) {
												System.out.println(parentProprety+"."+oldKey+"[]."+oldNestedObjectId+" deleted");
											}
										}
					   	       			
					   	       			// added nested objects
					   	       			//System.out.println(newNestedObjectIds);
					   	       		for (Long newNestedObjectId : newNestedObjectIds) {
										if(!oldNestedObjectIds.contains(newNestedObjectId)) {
											System.out.println(parentProprety+"."+oldKey+"[]."+newNestedObjectId+" added");
										}
									}
					   	       		
									}
			   	       			
			   	       		}
					    }
							
							
							
						}
							
					
				}
    			
    		}
    	
    	
    
    
    
}
