package Queries;

import Main.Data;

//query class for the question "Are there more users who looked at X than Y?"
public class QueryMoreUsers extends Thread {
	private int startNum = -1; //number user for which the tread should start from the main data set
	private int endNum = -1;//number user for which the tread should end at in the main data set
	private byte pageA = 0;//the page A that we want to see if the user viewed
	private byte pageB = 0;//the page B that we want to see if the user viewed
	private int viewsA = 0;//the number of times the user viewed page A
	private int viewsB = 0;//the number of times the user viewed page B
	
	//constructor for this query type, which involves passing in a Page A and a Page B
	public QueryMoreUsers(byte pA, byte pB) {
		pageA = pA;
		pageB = pB;
		startNum = Data.getNextStartNum();
		endNum = Data.getNextEndNum();
	}
	
	//the method which executes the query and determines how many times the user viewed page A and page B. it stores the information in fields.
	public void run() {
		int i = startNum;
		byte[][] data = Data.getStorage();
		while(i<=endNum) { //for each user record
			byte[] pagesViewed = data[i];
			boolean seenA = false;
			boolean seenB = false;
			for(byte b:pagesViewed) {//for each page in user record
				if(!seenA&&b==pageA) {
					viewsA = viewsA+1;
					seenA = true;
				}
				if(!seenB&&b==pageB) {
					viewsB = viewsB + 1;
					seenB = true;
				}
				if(seenA&&seenB) {
					break;
				}
			}
			i++;
			if(i%10000==0) yield(); //yields every 10000 records to avoid overhead but allow for other processes to run
		}
	}
	
	//returns the number of views of A
	public int getViewsA() {
		return viewsA;
	}
	
	//returns the number of views of B
	public int getViewsB() {
		return viewsB;
	}
}
