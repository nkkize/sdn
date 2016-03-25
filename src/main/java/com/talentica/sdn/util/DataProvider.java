package com.talentica.sdn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	//Shared Queue
	private static BlockingQueue<DataDictionary> sharedQueue = new LinkedBlockingQueue<DataDictionary>();
	//For overwhelming producer, create buffer to send data to UI
	private static List<DataDictionary> tempList = new LinkedList<>();
	//List to copy shuffle data for static post processing
	private static List<DataDictionary> analysisList = new LinkedList<DataDictionary>();

	//Starting Producer thread
	public void startProducer(String filePath) {
		File file = new File(filePath);
		Thread producer = new Thread(new Producer(file, sharedQueue, analysisList));
		producer.start();
	}

	//Starting Consumer thread
	public void startConsumer() {
		Thread consumer = new Thread(new Consumer(sharedQueue, tempList));
		consumer.start();
	}
	
	//Copying data from buffer list to send to UI
	public List<DataDictionary> copyAndResetList(List<DataDictionary> plotList) {
		for (DataDictionary dataDictionary : this.tempList) {
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
	
	//Copying data for post processing
	public void copyAnalysisList(List<DataDictionary> plotList) {
		Map<Integer, DataDictionary> map = new LinkedHashMap<>();
		for (DataDictionary dataDictionary : this.analysisList) {
			if (map.containsKey(dataDictionary.getTpDest())) {
				//removing value from map to have timeString of dataDictionary in insertion order
				map.remove(dataDictionary.getTpDest());
			}
			map.put(dataDictionary.getTpDest(), dataDictionary);
		}
		for (Entry<Integer, DataDictionary> entry : map.entrySet()) {
			plotList.add(entry.getValue());
		}
	}
}

// Producer
class Producer implements Runnable {
	private final BlockingQueue<DataDictionary> sharedQueue;
	private final List<DataDictionary> analysisList;
	private final Map<String, String> edgeConnectivityMap = Constants.edgeConnectivityMap;
	private static int readRec = 0;
	private File file;
	private BufferedReader bufferedReader;
	private FileReader fileReader;

	public Producer(File file, BlockingQueue<DataDictionary> sharedQueue, List<DataDictionary> analysisList) {
		this.file = file;
		this.sharedQueue = sharedQueue;
		this.analysisList = analysisList;
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
						if (values[1].equalsIgnoreCase(edgeConnectivityMap.get(values[3]))) {
							DataDictionary dataDictionary = new DataDictionary();
							dataDictionary.setTimeString(values[0]);
							dataDictionary.setNewSource(values[2]);
							dataDictionary.setNewDest(values[3]);
							dataDictionary.setByteCount(Integer.parseInt(values[6]));
							dataDictionary.setTpSource(Integer.parseInt(values[13]));
							dataDictionary.setTpDest(Integer.parseInt(values[14]));
							if (dataDictionary.getTpSource() == 13562)
								analysisList.add(dataDictionary);
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
				DataDictionary dataDictionary = (DataDictionary) sharedQueue.take();
				tempList.add(dataDictionary);
			} catch (Exception ex) {
				Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, "Exception occured while consuming data",
						ex);
			}
		}
	}
}
