package com.armory.app.logs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class MergeFiles {
	 
	

	public static void main(String[] args) throws IOException, ParseException {
		MergeFiles mergeFiles = new MergeFiles();
		SimpleDateFormat sdf = Constants.ISO;
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		//1) Get the fileNames
		System.out.println(" Get FileNames");
		List<String> listFilesNames = mergeFiles.retrieveListFiles("C:\\dev\\logs\\", "server-");
		
		listFilesNames.forEach(file -> System.out.println(file));
		
		
		//3) Create 3 lists to keep the data
		List<BufferedReader> readerList = new ArrayList<>();
	    List<String> lineList = new ArrayList<>();
	    List<Date>   dateList = new ArrayList<>();
		
	    //4) Populate lists according the number of files
	    mergeFiles.populateLists(listFilesNames, readerList, lineList, dateList, sdf);
	    
	    System.out.println(" List BufferedReaders");
	    readerList.forEach(buffer -> System.out.println(buffer));
	    System.out.println(" List firstLine");
	    lineList.forEach(line -> System.out.println(line));
	    System.out.println(" List dates");
	    dateList.forEach(date -> System.out.println(date));
	    
	    //8) Create a tempFile as Result
	    String mergeFileName = "C:\\dev\\logs\\expectedLog.txt";
	  	mergeFiles.mergeFiles(readerList, lineList, dateList, sdf, mergeFileName);
	  	System.out.println(" End Process on file: "+mergeFileName);

	}
	
	public List<String> retrieveListFiles(final String baseDirectory, final String fileName) throws IOException{
		 return Files.list(Paths.get(baseDirectory))
				 	.filter(file -> file.toString().contains(fileName))
				    .map(path -> path.toString())
			        .collect(Collectors.toList());
	}
	
	public void populateLists(List<String> listFilesNames, List<BufferedReader> readerList, List<String> lineList,  List<Date>  dateList, SimpleDateFormat sdf) throws IOException, ParseException {
		System.out.println(" Populating Lists");		 
		 for (String file : listFilesNames) {
		        // 5) Create BufferedReader list with respect to the file.
		        BufferedReader br1 = new BufferedReader(new FileReader(file));
		        readerList.add(br1);
		        // 6) Read the 1st line of each line and store it in another list.
		        String line = br1.readLine();
		        lineList.add(line);
		        // 7) Store the date from the 1st line of each file.
		        String dateString = line.substring(0, 24);
		        Date date = sdf.parse(dateString);
		        dateList.add(date);
		    }
	}	
	
	
	public void mergeFiles(List<BufferedReader> readerList, List<String> lineList,  List<Date>   dateList, SimpleDateFormat sdf, String expectedFile) throws ParseException, IOException {
		System.out.println(" Merging Lists in a file: ");	
		PrintWriter writer = new PrintWriter(expectedFile, "UTF-8");	
		int index = readerList.size();
	    // 9) While BufferedReader's size is not zero then,
	    while (index > 0) {
	        // 10) Take the index of minimum date from dateList
	        int indexMin = retrieveMinDateIndex(dateList);
	        // 11) Get the Line with the index you got in the previous step (lineToWrite).
	        String lineToWrite = lineList.get(indexMin);
	        writer.println(lineToWrite);
	        //System.out.println(lineToWrite);
	        // 12) Get the buffered reader with the index.
	        BufferedReader br1 = readerList.get(indexMin);
	        if (br1 != null) {
	            //  13) If the BR is not null then read the line.
	            String line = br1.readLine();
	            if (line != null) {
	                // 14)If line is not equal to null then remove the lineList from the index and add the line to index.
	                lineList.remove(indexMin);
	                lineList.add(indexMin, line);
	                if (line.length() > 24) {
	                    // 15)If the line length is greater than 24 (yyyy-MM-dd'T'HH:mm:ss,SSS'Z') then take the first 24 String from the line.
	                    String dateString = line.substring(0, 24);
	                    Matcher matcher = Constants.DATE_FORMAT_PATTERN.matcher(dateString);
	                    if (matcher.matches()) {
	                        // 16)Check if the date matches to the pattern, if matches then add the date to dateList
	                        Date date = sdf.parse(dateString);
	                        dateList.remove(indexMin);
	                        dateList.add(indexMin, date);
	                    }
	                }
	            } else {
	                //If line is null then remove the min indexed line from lineList,dateList,BufferedReader list. Do BufferedReader--.
	                lineList.remove(indexMin);
	                dateList.remove(indexMin);
	                readerList.remove(indexMin);
	                br1.close();
	                index--;
	            }
	        }
	    }
	    writer.close();	   
	}
	
	public int retrieveMinDateIndex(List<Date> dateList) {
	    // return index of min date
	    Date mintDate = dateList.get(0);
	    int minIndex = 0;
	    for (int i = 1; i < dateList.size(); i++) {
	        Date currentDate = dateList.get(i);
	        if (mintDate != null) {
	            if (currentDate != null) {
	                if (mintDate.after(currentDate)) {
	                    // We have a new minDate and minIndex
	                    mintDate = dateList.get(i);
	                    minIndex = i;
	                } else {
	                    // we keep current min
	                }
	            } else {
	                // we keep current min
	            }
	        } else {
	            mintDate = currentDate;
	            minIndex = i;
	        }
	    }
	    return minIndex;
	}

}
