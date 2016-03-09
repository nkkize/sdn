package com.talentica.sdn.service;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.talentica.sdn.model.DataDictionary;

public class ProducerConsumerPattern {

	public static void main(String args[]) {

		// Creating shared object
		BlockingQueue sharedQueue = new LinkedBlockingQueue();

		// Creating Producer and Consumer Thread
		Thread prodThread = new Thread(new Producer(sharedQueue));
		Thread consThread = new Thread(new Consumer(sharedQueue));

		// Starting producer and Consumer thread
		prodThread.start();
		consThread.start();
	}

}

// Producer Class in java
class Producer implements Runnable {

	private final BlockingQueue<DataDictionary> sharedQueue;
	File file = new File("D://sampleCSV.csv"); // The CSV file.
	String path = String.valueOf(file.getAbsoluteFile());
	File f = new File(path);
	BufferedInputStream bis;
	FileInputStream fis;

	public Producer(BlockingQueue sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {

		// read csv file per record and dump each record into queue
		/*
		 * for (int i = 0; i < 10; i++) { try { System.out.println("Produced: "
		 * + i); sharedQueue.put(i); } catch (Exception ex) {
		 * Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null,
		 * ex); } }
		 */
		while (true) {
			try {
				DataInputStream dis = null;
				String record = null;
				int recCount = 0;
				fis = new FileInputStream(f);
				bis = new BufferedInputStream(fis);
				dis = new DataInputStream(bis);
				record = dis.readLine();
				while ((record = dis.readLine()) != null) {
					recCount++;
					String values = record;
					String comma = ",";
					int location = values.indexOf(comma);
					String xvalue = values.substring(0, location);
					String yvalue = values.substring(location + 1);
					DataDictionary dataDictionary = new DataDictionary();
					dataDictionary.setSource(xvalue);
					dataDictionary.setByteCount(Integer.parseInt(yvalue));
					sharedQueue.put(dataDictionary);
				}
			} catch (Exception e) {
			}
		}

	}

}

// Consumer Class in Java
class Consumer implements Runnable {

	private final BlockingQueue sharedQueue;

	public Consumer(BlockingQueue sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Consumed: " + sharedQueue.take());
			} catch (Exception ex) {
				Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
