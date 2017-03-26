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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdministratorService adminService;
	
	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping("/action-1")
	public ModelAndView action1() {
		ModelAndView result;

		result = new ModelAndView("administrator/action-1");

		return result;
	}

	// Action-2 ---------------------------------------------------------------

	@RequestMapping("/action-2")
	public ModelAndView action2() {
		ModelAndView result;

		result = new ModelAndView("administrator/action-2");

		return result;
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
				
		result = new ModelAndView("administrator/dashboard");
		result.addObject("ratioDemand", adminService.ratioDemand());
		result.addObject("avgOffersCustomer", adminService.avgOffersCustomer());
		result.addObject("avgRequestsCustomer",adminService.avgRequestsCustomer());
		result.addObject("avgApplicationsOffer", adminService.avgApplicationsOffer());
		result.addObject("avgApplicationsRequest", adminService.avgApplicationsRequest());
		result.addObject("customerMoreApplicationAccepted", adminService.customerMoreApplicationAccepted().getName());
		result.addObject("customerMoreApplicationDenied", adminService.customerMoreApplicationDenied().getName());
		result.addObject("avgCommentPerActor", adminService.avgCommentPerActor());
		result.addObject("avgCommentPerOffer", adminService.avgCommentPerOffer());
		result.addObject("avgCommentPerRequest", adminService.avgCommentPerRequest());
		result.addObject("avgCommentPostAdmin", adminService.avgCommentPostAdmin());
		result.addObject("avgCommentPostCustomer", adminService.avgCommentPostCustomer());
		result.addObject("minSentMessagePerActor", adminService.minSentMessagePerActor());
		result.addObject("maxSentMessagePerActor", adminService.maxSentMessagePerActor());
		result.addObject("avgSentMessagePerActor", adminService.avgSentMessagePerActor());
		result.addObject("minReciveMessagePerActor",adminService.minReciveMessagePerActor());
		result.addObject("maxReciveMessagePerActor", adminService.maxReciveMessagePerActor());
		result.addObject("avgReciveMessagePerActor", adminService.avgReciveMessagePerActor());
		result.addObject("actorsSentMoreMessages", adminService.actorsSentMoreMessages());
		result.addObject("actorsGotMoreMessages", adminService.actorsGotMoreMessages());
		
		
		return result;
		
	}
	

}
