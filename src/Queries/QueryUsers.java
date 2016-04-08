package Queries;

import Main.Data;

//query class for the question "Are there more than __ users who looked at X?"
public class QueryUsers extends Thread {
	
	private int startNum = -1;//number user for which the tread should start from the main data set
	private int endNum = -1;//number user for which the tread should end at in the main data set
	private byte pageNum = 0;//the page number which we want to see if the user viewed
	private int totalViews = 0;//the number of users who meet the criteria for the query
	
	//constructor for the query that takes in the page number we want to count users who viewed
	public QueryUsers(byte pn) {
		startNum = Data.getNextStartNum();
		endNum = Data.getNextEndNum();
		pageNum = pn;
	}
	
	//method that runs the query and counts how many users looked at a given page.
	public void run() {
		int i = startNum;
		byte[][] data = Data.getStorage();
		while(i<=endNum) { //for each user record
			byte[] pagesViewed = data[i];
			for(byte b:pagesViewed) {
				if(b==pageNum) {
					totalViews = totalViews + 1;
					break;
				}
			}
			i++;
			if(i%10000==0) yield(); //yields every 10000 records to avoid overhead but allow for other processes to run
		}
	}
	
	//returns the total number of users who met the query criteria
	public int getTotalViews() {
		return totalViews;
	}
}
