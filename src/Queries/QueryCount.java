package Queries;

import Main.Data;

//query class for the question "How many users viewed X __ number of times?" 
public class QueryCount extends Thread {

	private int startNum = -1;//number user for which the tread should start from the main data set
	private int endNum = -1;//number user for which the tread should end at in the main data set
	private byte pageNum = 0; //this is the page which we are querying to see if the user viewed it
	private int numViewsMin = 0;  //this is the number of times that the user should view the page
	private int totalViews = 0; //this is the number of users who meet the query conditions
	
	//constructor for the query, which requires the page we want to see if people view and the number of views
	public QueryCount(byte pn, int views) {
		startNum = Data.getNextStartNum();
		endNum = Data.getNextEndNum();
		pageNum = pn;
		numViewsMin = views;
	}
	
	
	//the method to process the section of the data set, and see if the user viewed a page a certain number of times, then store the data in an instance variable
	public void run() {
		int i = startNum;
		byte[][] data = Data.getStorage();
		while(i<=endNum) { //for each user record
			byte[] pagesViewed = data[i];
			int countOfViews = 0;
			for(byte b:pagesViewed) {
				if(b==pageNum) {
					countOfViews = countOfViews + 1;
				}
			}
			if(countOfViews==numViewsMin) {
				totalViews = totalViews + 1;
			}
			i++;
			if(i%10000==0) yield(); //yields every 10000 records to avoid overhead but allow for other processes to run
		}
	}
	
	//returns the total views for an instance, which are added together in the main class
	public int getTotalViews() {
		return totalViews;
	}
}
