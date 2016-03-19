package com.talentica.sdn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.talentica.sdn.model.DataDictionary;

/**
 * @author NarenderK
 *
 */
public class DataProvider {
	// Creating shared object
	private static BlockingQueue<DataDictionary> sharedQueue = new LinkedBlockingQueue<DataDictionary>();
	private static List<DataDictionary> tempList = new ArrayList<>();
	
	// Starting Producer thread
	public void startProducer(String filePath) {
		File file = new File(filePath);
		Thread producer = new Thread(new Producer(file, sharedQueue));
		producer.start();
	}
	
	// Starting Consumer thread
	public void startConsumer(){
		Thread consumer = new Thread(new Consumer(sharedQueue, tempList));
		consumer.start();
	}
	
	public List<DataDictionary> copyAndResetList(List<DataDictionary> plotList){
		for (DataDictionary dataDictionary : this.tempList){
			plotList.add(dataDictionary);
		}
		this.tempList.clear();
		return plotList;
	}

	public static List<DataDictionary> getTempList() {
		return tempList;
	}

	public static void setTempList(List<DataDictionary> tempList) {
		DataProvider.tempList = tempList;
	}
}

//Producer
class Producer implements Runnable {
	private final BlockingQueue<DataDictionary> sharedQueue;
	File file;
	private static int readRec = 0;
	BufferedReader bufferedReader;
	FileReader fileReader;
	int count = 0;
	Map<String, String> edgeConnectivityMap  = Constants.edgeConnectivityMap;
	public Producer(File file, BlockingQueue<DataDictionary> sharedQueue) {
		this.file = file;
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String record = null;
				int recCount = 0;
				while ((record = bufferedReader.readLine()) != null) {
					recCount++;
					if (recCount > readRec) {
						String[] values = record.split(",");
						readRec++;
						if(values[1].equalsIgnoreCase(edgeConnectivityMap.get(values[3]))){
						DataDictionary dataDictionary = new DataDictionary();
						dataDictionary.setTimeString(values[0]);
						dataDictionary.setNewSource(values[2]);
						dataDictionary.setByteCount(Integer.parseInt(values[6]));
						dataDictionary.setTpSource(Integer.parseInt(values[13]));
						sharedQueue.put(dataDictionary);
						}
					}
				}
			} catch (Exception e) {
				Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, "Exception occured in producer", e);
			} finally {
				try {
					fileReader.close();
					bufferedReader.close();
				} catch (Exception e) {
					Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}
}

// Consumer
class Consumer implements Runnable {
	private final BlockingQueue<DataDictionary> sharedQueue;
	private List<DataDictionary> tempList;

	public Consumer(BlockingQueue<DataDictionary> sharedQueue, List<DataDictionary> tempList) {
		this.sharedQueue = sharedQueue;
		this.tempList = tempList;
	}

	@Override
	public void run() {
		while (true) {
			try {
				DataDictionary dataDictionary = (DataDictionary)sharedQueue.take();
				tempList.add(dataDictionary);
			} catch (Exception ex) {
				Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, "Exception occured while consuming data", ex);
			}
		}
	}
}
