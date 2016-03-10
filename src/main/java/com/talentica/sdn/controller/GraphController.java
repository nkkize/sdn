/**
 * 
 */
package com.talentica.sdn.controller;

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
		model.setViewName("welcome");
		return model;
	}
	
	@RequestMapping(value="/getData", method = RequestMethod.GET)
	public @ResponseBody List<DataDictionary> consumer() {
		DataProvider dataController = new DataProvider();
		//System.out.println(dataController.copyQueueIntoList());
		dataController.startConsumer();
		return dataController.getTempList();
	}	
}
