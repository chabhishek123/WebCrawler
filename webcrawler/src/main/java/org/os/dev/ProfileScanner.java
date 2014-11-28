package org.os.dev;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProfileScanner {
	/**
	 * 1. Read content ,create a file of profiles
	 * 2. Create a thread pool exe and pick each entry and download
	 */

	private static int profileCount ;
	private static final int NTHREDS = 20;

	public static void main(String[] args){
		createProfilesFile();
		downloadFiles();

	}

	private static void downloadFiles() {

		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		File file = new File("profileinfo.txt");

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] contents = line.split(","); 
				Runnable worker = new PicDownloader(contents[1],contents[0]);
				executor.execute(worker);
			}
			// This will make the executor accept no new threads
			// and finish all existing threads in the queue
			executor.shutdown();
			// Wait until all threads are finish
			executor.awaitTermination(30l,TimeUnit.MINUTES);
			scanner.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished all threads");

	}

	private static void createProfilesFile() {
		// Location of file to read
		File file = new File("content2.txt");

		try {

			Scanner scanner = new Scanner(file);
			StringBuilder profileInfo = new StringBuilder();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line.contains("JavaScript:viewPhoto")){
					String profileLine = line.substring(21);
					String[] tokens = profileLine.split("'");
					profileInfo.append(tokens[1]);
					profileInfo.append(",");
					profileInfo.append(tokens[3]);
					profileInfo.append("\n");

					profileCount++;

				}
			}
			scanner.close();
			//create a file
			File profileFile = new File("profileinfo.txt");

			// if file doesnt exists, then create it
			if (!profileFile.exists()) {
				profileFile.createNewFile();
			}

			FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(profileInfo.toString());
			bw.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
		System.out.println("Number of profiles : "+profileCount);
	}
}
