package Queries;

import Main.Data;

//query class for the question "What percent of users looked at X more than Y?"
public class QueryPercentLooked extends Thread {
	private int startNum = -1;//number user for which the tread should start from the main data set
	private int endNum = -1;//number user for which the tread should end at in the main data set
	private byte pageA = 0;//the page A which we want to see if the user viewed
	private byte pageB = 0;//the page B which we want to see if the user viewed
	private int totalViews = 0; //the total number of users who meet the criteria of the query
	
	//constructor for the query which takes in the page A and page B to query for
	public QueryPercentLooked(byte pA, byte pB) {
		startNum = Data.getNextStartNum();
		endNum = Data.getNextEndNum();
		pageA = pA;
		pageB = pB;
	}
	
	//the method that actually executes the query to check if the user viewed page A more than page B
	public void run() {
		int i = startNum;
		byte[][] data = Data.getStorage();
		while(i<=endNum) { //for each user record
			byte[] pagesViewed = data[i];
			int countOfA = 0;
			int countOfB = 0;
			//checks the views of pages
			for(byte b:pagesViewed) {
				if(b==pageA) {
					countOfA = countOfA + 1;
				}
				if(b==pageB) {
					countOfB = countOfB + 1;
				}
			}
			if(countOfA>countOfB) {
				totalViews = totalViews + 1;
			}
			i++;
			if(i%10000==0) yield(); //yields every 10000 records to avoid overhead but allow for other processes to run
		}
	}
	
	//returns the total users who viewed A more than B
	public int getPercentViews() {
		return totalViews;
	}
}
