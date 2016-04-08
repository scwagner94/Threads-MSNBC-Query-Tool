package Queries;

import Main.Data;

//query class for the question "What percent of users looked at X?"
public class QueryPercentage extends Thread {
	
	private int startNum = -1;//number user for which the tread should start from the main data set
	private int endNum = -1;//number user for which the tread should end at in the main data set
	private byte pageNum = 0;//the page number which we are trying to see if the user viewed
	private int totalViews = 0;//the total number of users who meet the criteria for the query
	
	//the constructor for the query which takes in the page number we want to see if a user viewed
	public QueryPercentage(byte page) {
		startNum = Data.getNextStartNum();
		endNum = Data.getNextEndNum();
		pageNum = page;
	}
	
	//executes the query and determines how many users viewed a certain page type
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
	
	//returns the total number of users who met the criteria for the query
	public int getPercentageViewed() {
		return totalViews;
	}
}
