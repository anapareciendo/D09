/*
 * ProfileController.java
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import domain.Customer;
import forms.ActorForm;

@Controller
@RequestMapping("/security")
public class SigninController extends AbstractController {
	
	//Supporting Services
	@Autowired
	private CustomerService customerService;
	
	
	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public ModelAndView signinUser(){
		ModelAndView result;
		ActorForm actor = new ActorForm();
		
		result = new ModelAndView("security/signin");
//		result.addObject("authority", "lessor2");
		result.addObject("customer", actor);
		
		return result;
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST, params = "customer")
	public ModelAndView user(ActorForm actor, BindingResult binding) {
		ModelAndView result;
		Customer customer;
		customer = customerService.reconstruct(actor, binding);

		if (binding.hasErrors() || customer.getName().equals("Pass") || customer.getName().equals("Cond")) {
			result = new ModelAndView("security/signin");
//			result.addObject("authority", "lessor2");
			result.addObject("customer", actor);
			if(customer.getName().equals("Pass")){
				result.addObject("message", "security.password.failed");
			}else if(customer.getName().equals("Cond")){
				result.addObject("message", "security.condition.failed");
			}
			else{
				result.addObject("errors", binding.getAllErrors());
			}
		} else {
			try{
				customerService.save(customer);
				result = new ModelAndView("redirect:login.do");
			}catch (Throwable oops) {
				result = new ModelAndView("security/signin");
				result.addObject("authority", "customer");
				result.addObject("customer", actor);
				result.addObject("message", "security.signin.failed");
			}
		}

		return result;
	}

}
