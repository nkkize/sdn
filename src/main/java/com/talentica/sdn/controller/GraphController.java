/**
 * 
 */
package com.talentica.sdn.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.talentica.sdn.util.GraphUtil;

/**
 * @author NarenderK
 *
 */
@Controller
public class GraphController {
	
	@Autowired
	private GraphUtil graphUtil;

	@RequestMapping("/")
	public ModelAndView greeting() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}
	
	@RequestMapping("/plotGraph")
	public ModelAndView plotGraph() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value="/plot", method=RequestMethod.POST)
    public void plot(@RequestParam("file") MultipartFile file, HttpServletResponse response){
        if (!file.isEmpty()) {
            try {
            	InputStream inputStream = file.getInputStream();
            	JFreeChart chart = graphUtil.draw(inputStream);
    	        int width = 500;
    	        int height = 300;
    	        ChartUtilities.writeChartAsPNG( response.getOutputStream(), chart, width, height );
    	        response.getOutputStream().close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            }
    }
	
}
