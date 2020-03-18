package com.armory.app.logs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class CreatingFileLogs {	
	
	private static final double MEG = (Math.pow(1024, 2));	

	public static void main(String[] args) throws InterruptedException, IOException {
		CreatingFileLogs creatingFileLogs = new CreatingFileLogs();
		List<File> logs = creatingFileLogs.createLogs(10, "c:/dev/logs");
		creatingFileLogs.writeFile(logs);
	}

	public List<File> createLogs(int numberFiles, String basePath) {
		StringBuilder fileName = null;
		File file = null;
		List<File> files = new ArrayList<File>();

		for (int index = 1; index <= numberFiles; index++) {
			fileName = new StringBuilder();
			int lengthLetters = 10;
			boolean useLetters = true;
			boolean useNumbers = true;

			String generatedString = RandomStringUtils.random(lengthLetters, useLetters, useNumbers);
			fileName.append(basePath).append("/server-").append(generatedString).append(".log");

			System.out.println(fileName.toString());
			file = new File(fileName.toString());

			try {
				file.createNewFile();
				files.add(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return files;
	}

	public void writeFile(List<File> files) throws InterruptedException, IOException {
		char character = 'A';

		for (File file : files) {

			WriteLogs fileWritter = new WriteLogs(file, character, (int) MEG);
			Thread thread = new Thread(fileWritter);
			thread.start();

			character++;

		}
	}
	
	
}
