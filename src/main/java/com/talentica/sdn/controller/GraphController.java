/**
 * 
 */
package com.talentica.sdn.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.model.DataDictionary;
import com.talentica.sdn.util.CommonUtil;
import com.talentica.sdn.util.DataProvider;

/**
 * @author NarenderK
 *
 */
@Controller
public class GraphController {
	
	private static String filePath;
	
	@RequestMapping(value = "/plot", method = RequestMethod.POST)
	public ModelAndView plotChart(@RequestParam("filePath") String filePath) {
		ModelAndView model = new ModelAndView();
		CommonUtil commonUtil = new CommonUtil();
		if(commonUtil.isNotEmpty(filePath)){
			this.filePath = filePath;
			DataProvider dataProvider = new DataProvider();
			System.out.println(filePath);
			dataProvider.startProducer(filePath);
			dataProvider.startConsumer();
			model.setViewName("plot");			
		}
		else
			model.setViewName("error");
		return model;
		
	}

	@RequestMapping("/")
	public ModelAndView plotGraph() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}
	
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> consumer() {
		List<DataDictionary> plotList = new ArrayList<DataDictionary>();
		DataProvider dataProvider = new DataProvider();
		dataProvider.copyAndResetList(plotList);
		return plotList;
	}

	@RequestMapping(value = "/loadCSV", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> loadData() {
		List<DataDictionary> plotList = new ArrayList<>();
		File file = new File(filePath);
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
