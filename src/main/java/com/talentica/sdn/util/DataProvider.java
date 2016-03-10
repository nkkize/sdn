package com.talentica.sdn.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.talentica.sdn.model.DataDictionary;

public class DataProvider {
	// Creating shared object
	private static BlockingQueue sharedQueue = new LinkedBlockingQueue();
	private static List<DataDictionary> tempList = new ArrayList<>();
	private static List<DataDictionary> startList = new ArrayList<>();
	File file = new File("D:\\sampleCSV.csv");
	// Creating Producer and Consumer Thread
	private Producer tailer = new Producer(file, 1000, false, sharedQueue);
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
		DataProvider.startList = startList;
	}

	public void startProducer() {
		tailer.start();
	}
	
	public void startConsumer(){
		// Starting Consumer thread
		consThread.start();
	}
	
	public List copyQueueIntoList(){
		List<DataDictionary> startList = new ArrayList<>();
		Object object = null;
		while ((object=this.sharedQueue.poll())!=null)
			startList.add((DataDictionary)object);
		return startList;
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
