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
import services.CustomerService;
import services.DemandService;
import domain.Comment;
import domain.Customer;
import domain.Demand;

@Controller
@RequestMapping("/display")
public class DisplayController extends AbstractController {

	@Autowired
	private DemandService demandService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CommentService commentService;
	
	// Constructors -----------------------------------------------------------

	public DisplayController() {
		super();
	}


	@RequestMapping(value = "/demand", method = RequestMethod.GET)
	public ModelAndView displayDemand(@RequestParam int demandId) {
		ModelAndView result; 
		
		Demand demand = demandService.findOne(demandId);
		Set<Comment> comments = new HashSet<Comment>();
		comments.addAll(commentService.findRealComments(demandId));
		
		result = new ModelAndView("demand/display");
		result.addObject("demand", demand);
		result.addObject("comments", comments);
		
		return result;
		
	}
	
	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public ModelAndView displayCustomer(@RequestParam int customerId) {
		ModelAndView result; 
		
		Customer customer = customerService.findOne(customerId);
		Set<Comment> comments = new HashSet<Comment>();
		comments.addAll(commentService.findRealComments(customerId));
		
		result = new ModelAndView("customer/display");
		result.addObject("actor", customer);
		result.addObject("comments", comments);
		
		return result;
		
	}
	

}
