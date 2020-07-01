/* General description of the class:
 * Creates an ArrayList with an inner ArrayList to store contact info for EACH contact.
 * Also stores the attributes present in the contact file.
 */
package application;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
		
public class Contacts {
	
    static ArrayList<ArrayList<String>> ContactList = new ArrayList<ArrayList<String>>();
    static ArrayList<String> attributes = new ArrayList<String>();

    public static void list(ArrayList<String> tagsContent, String contactCsvPath) {
    	try {
    		Scanner fileScan = new Scanner(new FileInputStream(contactCsvPath));
    		for(String tag : tagsContent)
    			attributes.add(tag);

    		fileScan.nextLine();
    		while(fileScan.hasNextLine()) {
    			String tempData = fileScan.nextLine();
    			String[] splitTags = tempData.split(",");
    			ArrayList<String> data = new ArrayList<String>();
    			for(String tag : splitTags)
    				data.add(tag);
    				ContactList.add(data);
    		}    	
    		fileScan.close();
    	}
    	catch(Exception e) {
    		ErrorHandle.generalExceptionError();
    	}
     }
        
}
