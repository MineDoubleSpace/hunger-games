package com.skitscape.sg.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class Download {
	
	public static void start(String link, File download) {
		Log.log("--- Getting ready to download map ---");
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int filesize = connection.getContentLength();
			float totalDataRead = 0;
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream());
			java.io.FileOutputStream fos = new java.io.FileOutputStream(download);
			java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int i = 0;
			while ((i = in.read(data, 0, 1024)) >= 0) {
				totalDataRead = totalDataRead + i;
				bout.write(data, 0, i);
				float percent = ((totalDataRead * 100) / filesize);
				Random rn = new Random();
				int rni = rn.nextInt(200);
				if (rni == 2) {
					Log.log("Downloading map: " + percent + "%");
				}

			}
			bout.close();
			in.close();
			Log.log("--- Map downloaded ---");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
