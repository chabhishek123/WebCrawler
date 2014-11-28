package org.os.dev;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class PicDownloader implements Runnable {

	private String id,pic;
	private String domainUrl ="";
	private String destinationPath =  "C:\\abhishek\\projects\\profilescanner-2014-11-23\\profilescanner\\profiles";   // linux - "/home/abch/abhishek/projects/profiles";

	public PicDownloader(String id, String pic) {
		this.id =id;
		this.pic =pic;
	}
	public void run() {
		// Hit the url save the file
		try {
			saveImage(domainUrl+pic,destinationPath+"/"+id+".jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

}
