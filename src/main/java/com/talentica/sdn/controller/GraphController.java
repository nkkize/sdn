/**
 * 
 */
package com.talentica.sdn.controller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.model.DataDictionary;
import com.talentica.sdn.util.GraphUtil;

/**
 * @author NarenderK
 *
 */
@Controller
public class GraphController {

	@Autowired
	private GraphUtil graphUtil;

	@RequestMapping("/chart")
	public ModelAndView greeting() {
		ModelAndView model = new ModelAndView();
		model.setViewName("chart");
		return model;
	}
	
	@RequestMapping("/chart2")
	public ModelAndView greetingSS() {
		ModelAndView model = new ModelAndView();
		model.setViewName("chart2");
		return model;
	}

	@RequestMapping("/plotGraph")
	public ModelAndView plotGraph() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}
	
	@RequestMapping("/high")
	public ModelAndView csv() {
		ModelAndView model = new ModelAndView();
		model.setViewName("highchartCSV");
		return model;
	}
	
	@RequestMapping("/file")
	public File file() {
		return new File("D://sampleCSV.csv");
	}

	@RequestMapping(value = "/plot", method = RequestMethod.POST)
	public void plot(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		if (!file.isEmpty()) {
			try {
				InputStream inputStream = file.getInputStream();
				JFreeChart chart = graphUtil.draw(inputStream);
				int width = 1000;
				int height = 800;
				ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
				response.getOutputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/getData", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> getShopInJSON() {
		List<DataDictionary> dataDictionaryList = new ArrayList<DataDictionary>();
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setSource("18:01:00");
		dataDictionary.setByteCount(10000);
		DataDictionary dataDictionary2 = new DataDictionary();
		dataDictionary2.setSource("18:01:02");
		dataDictionary2.setByteCount(10001);
		dataDictionaryList.add(dataDictionary);
		dataDictionaryList.add(dataDictionary2);
		return dataDictionaryList;
	}
}
