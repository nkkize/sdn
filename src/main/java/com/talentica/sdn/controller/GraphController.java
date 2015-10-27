/**
 * 
 */
package com.talentica.sdn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author NarenderK
 *
 */
@Controller
public class GraphController {

	@RequestMapping("/welcome")
	public ModelAndView greeting() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}

}
