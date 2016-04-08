package Main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Queries.*;

//main class that executes the program and runs the main dispatch loop
//by Sean Wagner
public class Main {
	
	//main method that reads the file and starts the gui
	public static void main(String[] args) throws IOException, InterruptedException {
		Reader.readFile(); //reads in the file to get the data
		MainGUI.launch(null);
	}
	
	
	//this is the master dispatch thread which controls which thread type is created and executed. often times, 
	//some inputs are left empty depending on which query is executed. the return type is a string that is directly output to the main gui.
	//type is type of query, x is a byte representing the first page type, y is a byte representing the second page type, textField is the input value integer
	public static String executeQuery(String type, byte x, byte y, int textField) throws InterruptedException {
		String[] pageNames = new String[] {"", "Front Page", "News", "Technology", "Local", "Opinion", "On-Air", "Miscellaneous", "Weather", "MSN News", "Health", "Living", "Business", "MSN Sports", "Sports", "Summary", "BBS", "Travel"};
		String retVal = "";
		//breakout for 1st query type
		if(type.equals("QueryPercentage")) { //what percent of users looked at X
			ArrayList<QueryPercentage> threads = new ArrayList<QueryPercentage>();
			while(Data.needsThread()) {//create the threads to run query
				//get value from GUI here
				byte pageNum = x;
				QueryPercentage qp = new QueryPercentage(pageNum);
				threads.add(qp);
			}
			//join threads and execute
			for(Thread t:threads) {
				t.start();
			}
			for(Thread t:threads) {
				t.join();
			}
			//makes sure that the decimal is displayed properly
			DecimalFormat df = new DecimalFormat("#.##");
			int TotalViews = 0;
			for(QueryPercentage qp : threads) {
				TotalViews = TotalViews + qp.getPercentageViewed();
			}
			double viewPercentage = ((TotalViews+0.0)/(Data.getDataSetSize()+0.0))*100.0;
			retVal = df.format(viewPercentage);
		}
		//breakout for 2nd query type
		else if(type.equals("QueryUsers")) { //are there more than ___ users who look at X
			ArrayList<QueryUsers> threads = new ArrayList<QueryUsers>();
			while(Data.needsThread()) {//create the threads to run query
				//get value from GUI here
				byte pageNum = x;
				QueryUsers qu = new QueryUsers(pageNum);
				threads.add(qu);
			}
			//join threads and execute
			for(Thread t:threads) {
				t.start();
			}
			for(Thread t:threads) {
				t.join();
			}
			int totalViews = 0;
			for(QueryUsers qu:threads) {
				totalViews = totalViews + qu.getTotalViews();
			}
			//have users stored in text field at this point, modify output
			retVal = String.valueOf(totalViews);
		}
		//breakout for 3rd query type
		else if(type.equals("QueryMoreUsers")) {
			ArrayList<QueryMoreUsers> threads = new ArrayList<QueryMoreUsers>();
			while(Data.needsThread()) {//create the threads to run query
				//get value from GUI here
				byte pageNum1 = x;
				byte pageNum2 = y;
				QueryMoreUsers qu = new QueryMoreUsers(pageNum1,pageNum2);
				threads.add(qu);
			}
			//join threads and execute
			for(Thread t:threads) {
				t.start();
			}
			for(Thread t:threads) {
				t.join();
			}
			String outVal;
			int viewsA = 0;
			int viewsB = 0;
			for(QueryMoreUsers qmu:threads) {
				viewsA = viewsA + qmu.getViewsA();
				viewsB = viewsB + qmu.getViewsB();
			}
			//determines if the user viewed A or B more
			if(viewsA>viewsB) {
				outVal = "Yes - " + viewsA +" "+pageNames[x] + " views vs. " + viewsB +" "+pageNames[y] + " views.";
			}
			else {
				outVal = "No - " + viewsA +" "+pageNames[x] + " views vs. " + viewsB +" "+pageNames[y] + " views.";
			}
			
			retVal = outVal;
		}
		//breakout for 4th query type
		else if(type.equals("QueryCount")) {
			ArrayList<QueryCount> threads = new ArrayList<QueryCount>();
			while(Data.needsThread()) {//create the threads to run query
				//get value from GUI here
				byte pageNum = x;
				//get other value from GUI here
				int numViews = textField;
				QueryCount qc = new QueryCount(pageNum,numViews);
				threads.add(qc);
			}
			//join threads and execute
			for(Thread t:threads) {
				t.start();
			}
			for(Thread t:threads) {
				t.join();
			}
			int countReturn = 0;
			for(QueryCount qc:threads) {
				countReturn = countReturn + qc.getTotalViews();
			}
			retVal = String.valueOf(countReturn);
		}
		//breakout for 5th query type
		else if(type.equals("QueryPercentLooked")) {
			ArrayList<QueryPercentLooked> threads = new ArrayList<QueryPercentLooked>();
			while(Data.needsThread()) {//create the threads to run query
				//get value from GUI here
				byte pageA = x;
				//get other value from GUI here
				byte pageB = y;
				QueryPercentLooked qc = new QueryPercentLooked(pageA,pageB);
				threads.add(qc);
			}
			//join threads and execute
			for(Thread t:threads) {
				t.start();
			}
			for(Thread t:threads) {
				t.join();
			}
			//decimal format for output
			DecimalFormat df = new DecimalFormat("#.##");
			int result = 0;
			for(QueryPercentLooked qpl:threads) {
				result = result + qpl.getPercentViews();
			}
			double answer = ((result+0.0)/(Data.getDataSetSize()+0.0))*100.0;//calculates the percentage from return values
			retVal = df.format(answer);
		}
		//should never get down here, something went really wrong
		else {
			retVal = "Nothing Done, ERROR";
		}
		//resets the data class to execute another query
		Data.resetStartEnd();
		return retVal;
	}
}
