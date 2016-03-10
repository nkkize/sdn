/**
 * 
 */
package com.talentica.sdn.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.model.DataDictionary;
import com.talentica.sdn.util.DataProvider;

/**
 * @author NarenderK
 *
 */
@Controller
public class GraphController {

	@RequestMapping("/plot")
	public ModelAndView plotChart() {
		ModelAndView model = new ModelAndView();
		model.setViewName("plot");
		return model;
	}

	@RequestMapping("/")
	public ModelAndView plotGraph() {
		ModelAndView model = new ModelAndView();
		DataProvider dataController = new DataProvider();
		dataController.startProducer();
		dataController.startConsumer();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> consumer() {
		List<DataDictionary> plotList = new ArrayList<DataDictionary>();
		DataProvider dataController = new DataProvider();
		dataController.copyAndResetList(plotList);
		return plotList;
	}

	@RequestMapping(value = "/loadCSV", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> loadData() {
		List<DataDictionary> plotList = new ArrayList<>();
		File file = new File("D:\\sampleCSV.csv");
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String record = null;
			int recCount = 0;
			while ((record = bufferedReader.readLine()) != null) {
				recCount++;
				String[] values = record.split(",");
				String timeString = values[0];
				String byteCount = values[6];
				DataDictionary dataDictionary = new DataDictionary();
				dataDictionary.setTimeString(timeString.substring(0, timeString.indexOf(".")));
				dataDictionary.setByteCount(Integer.parseInt(byteCount));
				plotList.add(dataDictionary);
			}
		} catch (Exception e) {
		} finally {
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return plotList;
	}
}
