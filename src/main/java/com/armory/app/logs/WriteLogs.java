package com.armory.app.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

public class WriteLogs implements Runnable {	
	private int totalRecordsSize = 0;
	private String threadName;	
	
	private File file;
	private Character character;
	private int bufSize;	
	
	public WriteLogs(File file, Character character, int bufSize) throws IOException {
		this.file = file;
		this.character = character;
		this.bufSize = bufSize;
		
		if(!file.exists()) {
			file.createNewFile();
		}	
	}
	
	@Override
	public void run() {
		try {
			threadName = Thread.currentThread().getName();
			System.out.println("Starting "+threadName);
			
			Thread.currentThread().sleep(RandomUtils.nextLong(1, 20)*1000);
			writeLog(String.format("Server %s: Started.", character));
			Thread.currentThread().sleep(RandomUtils.nextLong(1, 10)*1000);
			writeLogWithBuffer();
			writeLog(String.format("Server %s: Completed job", character));
			Thread.currentThread().sleep(RandomUtils.nextLong(1, 10)*1000);
			writeLog(String.format("Server %s: Terminated", character));
			Thread.currentThread().sleep(RandomUtils.nextLong(1, 10)*1000);
			
			 System.out.println("End: "+threadName);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void writeLogWithBuffer() throws IOException{
		List<String> records = new ArrayList<String>(Constants.RECORD_COUNT);
	   
	    
	    for (int i = 0; i < Constants.RECORD_COUNT; i++) {	    	   	
	    	records.add(Constants.RECORD);   
	    }	    

	    writeBuffered(records, bufSize);	   
	    
	    System.out.println(threadName+" "+records.size() + " 'records'");
	    System.out.println(threadName+" "+totalRecordsSize / Constants.MEG + " MB");
	}
	
	
	public void writeLog(String message) {		
		String dateTime = ZonedDateTime.now().format(Constants.IS2O);
		
		StringBuilder stringBuilder = new StringBuilder();
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
			stringBuilder.append(dateTime);
			stringBuilder.append(",");
			stringBuilder.append(message);
			writer.println(stringBuilder.toString());
				
		} catch (IOException e) {
					e.printStackTrace();
					
		}
	}
	
	private void writeBuffered(List<String> records, int bufSize) throws IOException {
		
	    try {
	        FileWriter writer = new FileWriter(file, true);
	        BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

	        System.out.println(threadName+" Writing buffered (buffer size: " + bufSize + ")... ");
	        write(records, bufferedWriter);
	    } finally {
	        // comment this out if you want to inspect the files afterward
	        //file.delete();
	    }
	}
	
	private void write(List<String> records, Writer writer) throws IOException {
	    long start = System.currentTimeMillis();
	    StringBuilder stringBuilder = null;
	    for (String record: records) {
	    	stringBuilder = new StringBuilder();
	    	String dateTime = ZonedDateTime.now().format(Constants.IS2O);
	    	stringBuilder.append(dateTime)
	    		.append(",")
	    		.append(String.format("Server %s: ", character))
	    		.append(Constants.RECORD);
	    	
	        writer.write(stringBuilder.toString());
	        totalRecordsSize+= stringBuilder.toString().getBytes().length;
	    }
	    writer.flush();
	    writer.close();
	    long end = System.currentTimeMillis();
	    System.out.println(threadName+" "+(end - start) / 1000f + " seconds");
	}

}
