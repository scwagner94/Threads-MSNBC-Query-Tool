package Main;

import java.io.IOException;
//class that holds the data for the entire program and also controls the tread start and stop numbers
public class Data {
	
	private static byte[][] mainStorage;//this is the main array that holds the read in data. Each row is a user, and each column holds a page view as a byte
	private static Data d; //partial singleton class implementation, holds an instance of the data
	private static int inputFileLen = -1; //10000; //989818 //this is the input file length
	final private static String fileName = "msnbc990928.txt"; //"testdata.txt"; //"msnbc990928.txt" //this is the dataset title to be read in
	final private static int threadCalcLen = 100000; //the main size of threads. each thread (up to the final one) is this large. the last thread is dynamically sized.
	private static int currentStartNum = 0; //number for the next thread to start at
	private static int currentEndNum = -1; //number for the next thread to stop at
	private static boolean needsThread = true; //determines if another thread is needed
	
	//constructor for the data class, initializes the main storage
	private Data() {
		mainStorage = new byte[getDataSetSize()][];
		//need to initialize each byte array, need to control sized based on input line
	}
	
	//returns the main storage of the program for the reader to input 
	public static byte[][] getStorage() {
		if(d==null) {
			d = new Data();
		}
		return mainStorage;
	}
	
	//returns the file name to read
	public static String getFileName() {
		return fileName;
	}
	
	//gets the size of the data set, defaults to the known input size of the primary data set
	public static int getDataSetSize() {
		if(inputFileLen==-1) {
			try {
				inputFileLen = Reader.getFileLen();
			} catch (IOException e) {
				inputFileLen = 989818;
			}
		}
		return inputFileLen;
	}
	
	//gets the next number for a new thread to start at, controlled by data class to ensure whole file is covered
	public static int getNextStartNum(){
		int retVal = currentStartNum;
		if(currentStartNum==0) {
			currentStartNum++;
		}
		currentStartNum = currentStartNum + threadCalcLen;
		return retVal;
	}
	
	//gets the next number for a new thread to end at, controlled by data class to ensure whole file is covered. it also handles controlling the length of the final thread which is a different size than the rest
	public static int getNextEndNum() {
		if(currentEndNum==-1) {
			currentEndNum = threadCalcLen;
		}
		int fileSize = Data.getDataSetSize();
		int retVal = currentEndNum;
		if(currentEndNum>=fileSize) {
			retVal = fileSize - 1;
			needsThread = false;
			return retVal;
		}
		currentEndNum = currentEndNum + threadCalcLen;
		return retVal;
	}
	
	//tells the thread creator if another thread is necessary
	protected static boolean needsThread() {
		return needsThread;
	}
	
	//resets the thread start and end controllers for another query
	public static void resetStartEnd() {
		currentStartNum = 0;
		currentEndNum = -1;
		needsThread = true;
	}
	
}
