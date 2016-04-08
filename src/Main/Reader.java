package Main;

import java.io.*;
//reader class which brings in the file and loads it into the 2D array for querying
public class Reader {

	//reads the entire file and determines its length to instantiate the array
	protected static int getFileLen() throws IOException {
		int fileLen = 0;
		FileReader fr;
		try {
			fr = new FileReader(Data.getFileName());
		} catch (FileNotFoundException e) {
			return 0;
		}
		//starts reader and reads the file
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			@SuppressWarnings("unused")
			String line;
			try {
				line = br.readLine();
				fileLen = fileLen + 1;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileLen;
	}
	
	//reads the entire data set file and loads into the main storage 2 dimmensional array. the rows are each user and the columns are pages viewed by the user
	protected static void readFile() throws IOException {
		byte[][] mainStore = Data.getStorage();
		int mainStorePointer = 0;
		FileReader fr;
		//gets the file setup
		try {
			fr = new FileReader(Data.getFileName());
		} catch (FileNotFoundException e) {
			return;
		}
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()) {
			String line;
			//reads the file and loads into the array
			try {
				line = br.readLine();
				String[] pageStrings = line.split(" ");
				byte[] pages = new byte[pageStrings.length];
				for(int i = 0; i < pageStrings.length; i++) {
					String s = pageStrings[i];
					pages[i] = Byte.parseByte(s);
				}
				mainStore[mainStorePointer] = pages;
				mainStorePointer = mainStorePointer + 1;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			//System.out.println(mainStorePointer);
		}
		
		
		
	}
	
}
