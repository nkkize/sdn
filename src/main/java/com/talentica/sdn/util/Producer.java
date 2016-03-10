package com.talentica.sdn.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingQueue;

import com.talentica.sdn.model.DataDictionary;

public class Producer extends Thread {

	private final BlockingQueue<DataDictionary> sharedQueue;
	private long sampleInterval = 5000;
	private File logfile;
	private boolean startAtBeginning = false;
	private boolean tailing = false;

	@SuppressWarnings("unchecked")
	public Producer(File file, long sampleInterval, boolean startAtBeginning, BlockingQueue sharedQueue) {
		this.sharedQueue = sharedQueue;
		this.logfile = file;
		this.sampleInterval = sampleInterval;
	}

	public void stopTailing() {
		this.tailing = false;
	}

	public void run() {
		long filePointer = 0;
		if (this.startAtBeginning) {
			filePointer = 0;
		} else {
			filePointer = this.logfile.length();
		}

		try {
			this.tailing = true;
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			while (this.tailing) {
				try {
					long fileLength = this.logfile.length();
					if (fileLength < filePointer) {
						file = new RandomAccessFile(logfile, "r");
						filePointer = 0;
					}
					if (fileLength > filePointer) {
						file.seek(filePointer);
						String line = file.readLine();
						while (line != null) {
							String values = line;
							String comma = ",";
							int location = values.indexOf(comma);
							String xvalue = values.substring(0, location);
							String yvalue = values.substring(location + 1);
							DataDictionary dataDictionary = new DataDictionary();
							dataDictionary.setSource(xvalue);
							dataDictionary.setByteCount(Integer.parseInt(yvalue));
							sharedQueue.put(dataDictionary);
							line = file.readLine();
						}
						filePointer = file.getFilePointer();
					}
					sleep(this.sampleInterval);
				} catch (Exception e) {
				}
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
