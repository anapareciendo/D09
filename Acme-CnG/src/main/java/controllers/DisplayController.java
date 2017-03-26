/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.DemandService;
import domain.Comment;
import domain.Demand;

@Controller
@RequestMapping("/display")
public class DisplayController extends AbstractController {

	@Autowired
	private DemandService demandService;
	@Autowired
	private CommentService commentService;
	
	// Constructors -----------------------------------------------------------

	public DisplayController() {
		super();
	}


	@RequestMapping(value = "/demand", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int demandId) {
		ModelAndView result; 
		
		Demand demand = demandService.findOne(demandId);
		Set<Comment> comments = new HashSet<Comment>();
		comments.addAll(commentService.findRealComments(demandId));
		
		result = new ModelAndView("demand/display");
		result.addObject("demand", demand);
		result.addObject("comments", comments);
		
		return result;
		
	}
	

}
