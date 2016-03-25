/**
 * 
 */
package com.talentica.viewsdn.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.talentica.viewsdn.model.DataDictionary;
import com.talentica.viewsdn.util.CommonUtil;
import com.talentica.viewsdn.util.Constants;
import com.talentica.viewsdn.util.DataProvider;

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
		if(CommonUtil.isNotEmpty(filePath)){
			this.filePath = filePath;
			DataProvider dataProvider = new DataProvider();
			dataProvider.startProducer(filePath);
			dataProvider.startConsumer();
			model.setViewName("plot");			
		}
		else
			model.setViewName("error");
		return model;
		
	}
	
	@RequestMapping(value = "/analysis")
	public ModelAndView showAnalysis() {
		ModelAndView model = new ModelAndView();
		model.setViewName("analysis");
		return model;
	}
	
	@RequestMapping("/aboutus")
	public ModelAndView showAboutUs() {
		ModelAndView model = new ModelAndView();
		model.setViewName("aboutus");
		return model;
	}

	@RequestMapping("/")
	public ModelAndView showHome() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}
	
	@RequestMapping(value = "/getData", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> getData() {
		List<DataDictionary> plotList = new ArrayList<DataDictionary>();
		DataProvider dataProvider = new DataProvider();
		dataProvider.copyAndResetList(plotList);
		return plotList;
	}
	
	@RequestMapping(value = "/analyse", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> analyseData() {
		List<DataDictionary> plotList = new LinkedList<DataDictionary>();
		DataProvider dataProvider = new DataProvider();
		dataProvider.copyAnalysisList(plotList);
		return plotList;
	}
	
	@RequestMapping("/dashboard")
	public RedirectView dashboardRedirect() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl(Constants.DASHBOARD_SERVER);
	    return redirectView;
	}
	
	@RequestMapping("/topology")
	public RedirectView topologyRedirect() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl(Constants.TOPOLOGY_SERVER);
	    return redirectView;
	}

	@RequestMapping(value = "/loadCSV", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> loadInitialData() {
		List<DataDictionary> plotList = new ArrayList<>();
		File file = new File(filePath);
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		Map<String, String> edgeConnectivityMap  = Constants.edgeConnectivityMap; 
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String record = null;
			int recCount = 0;
			while ((record = bufferedReader.readLine()) != null) {
				recCount++;
				String[] values = record.split(",");
				if(values[1].equalsIgnoreCase(edgeConnectivityMap.get(values[3]))){
				DataDictionary dataDictionary = new DataDictionary();
				dataDictionary.setTimeString(values[0]);
				dataDictionary.setNewSource(values[2]);
				dataDictionary.setNewDest(values[3]);
				dataDictionary.setByteCount(Integer.parseInt(values[6]));
				dataDictionary.setTpSource(Integer.parseInt(values[13]));
				dataDictionary.setTpDest(Integer.parseInt(values[14]));
				plotList.add(dataDictionary);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, "Exception occured while loading initial data", e);
		} finally {
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (Exception e) {
				Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return plotList;
	}
}
