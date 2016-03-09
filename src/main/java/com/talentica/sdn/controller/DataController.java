package com.talentica.sdn.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.talentica.sdn.model.DataDictionary;

public class DataController {
	// Creating shared object
	private static BlockingQueue sharedQueue = new LinkedBlockingQueue();
	private static List<DataDictionary> tempList = new ArrayList<>();
	private static List<DataDictionary> startList = new ArrayList<>();
	// Creating Producer and Consumer Thread
	private Thread prodThread = new Thread(new Producer(sharedQueue));
	private Thread consThread = new Thread(new Consumer(sharedQueue,tempList));

	public BlockingQueue getSharedQueue() {
		return sharedQueue;
	}

	public void setSharedQueue(BlockingQueue sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	public List<DataDictionary> getTempList() {
		return tempList;
	}

	public void setTempList(List<DataDictionary> tempList) {
		this.tempList = tempList;
	}

	public static List<DataDictionary> getStartList() {
		return startList;
	}

	public static void setStartList(List<DataDictionary> startList) {
		DataController.startList = startList;
	}

	public void startProducer() {
		// Starting producer thread
		prodThread.start();
	}
	
	public void startConsumer(){
		// Starting Consumer thread
		consThread.start();
	}
	
	public List copyQueueIntoList(){
		List<DataDictionary> startList = new ArrayList<>();
		Object object = null;
		while ((object= this.sharedQueue.poll())!=null)
			startList.add((DataDictionary)object);
		return startList;
	}
}

// Producer
class Producer implements Runnable {
	private final BlockingQueue<DataDictionary> sharedQueue;
	File file = new File("D://sampleCSV.csv"); // The CSV file.
	String path = String.valueOf(file.getAbsoluteFile());
	File f = new File(path);
	BufferedInputStream bis;
	FileInputStream fis;
	DataInputStream dis;
	private static int readRec = 0;

	public Producer(BlockingQueue sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String record = null;
				int recCount = 0;
				fis = new FileInputStream(f);
				bis = new BufferedInputStream(fis);
				dis = new DataInputStream(bis);
				record = dis.readLine();
				while ((record = dis.readLine()) != null) {
					recCount++;
					if (recCount > readRec) {
						String values = record;
						String comma = ",";
						int location = values.indexOf(comma);
						String xvalue = values.substring(0, location);
						String yvalue = values.substring(location + 1);
						DataDictionary dataDictionary = new DataDictionary();
						dataDictionary.setSource(xvalue);
						dataDictionary.setByteCount(Integer.parseInt(yvalue));
						readRec++;
						System.out.println(readRec);
						sharedQueue.put(dataDictionary);
					}
				}
			} catch (Exception e) {
			} finally {
				try {
					dis.close();
					bis.close();
					fis.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

// Consumer
class Consumer implements Runnable {
	private final BlockingQueue sharedQueue;
	private List tempList;

	public Consumer(BlockingQueue sharedQueue,List tempList) {
		this.sharedQueue = sharedQueue;
		this.tempList = tempList;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object obj = sharedQueue.take();
				tempList.clear();
				tempList.add(obj);
				System.out.println("Consumed: " + sharedQueue.take());
			} catch (Exception ex) {
				Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
