package controllers.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import controllers.AbstractController;
import domain.Application;
import domain.Type;

@Controller
@RequestMapping("/application/customer")
public class ApplicationCustomerController extends AbstractController{

	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value = "/offer", method = RequestMethod.GET)
	public ModelAndView offer() {
		ModelAndView result;
		List<Application> apps = new ArrayList<Application>();
		
		apps.addAll(applicationService.findApplicationMyOffers());
		
		result = new ModelAndView("application/list");
		result.addObject("requestURI", "application/customer/offer.do");
		result.addObject("apps", apps);

		return result;
	}
	
	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public ModelAndView request() {
		ModelAndView result;
		List<Application> apps = new ArrayList<Application>();
		
		apps.addAll(applicationService.findApplicationMyRequests());
		
		result = new ModelAndView("application/list");
		result.addObject("requestURI", "application/customer/requests.do");
		result.addObject("apps", apps);

		return result;
	}
	
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam int appId) {
		ModelAndView result;
	
		Type type =applicationService.accept(appId);
		
		if(type==Type.OFFER){
			result = new ModelAndView("redirect:offer.do");
		}else{
			result = new ModelAndView("redirect:request.do");
		}
		return result;
	}
	

	@RequestMapping(value = "/deny", method = RequestMethod.GET)
	public ModelAndView deny(@RequestParam int appId) {
		ModelAndView result;
	
		Type type =applicationService.deny(appId);
		
		if(type==Type.OFFER){
			result = new ModelAndView("redirect:offer.do");
		}else{
			result = new ModelAndView("redirect:request.do");
		}
		return result;
	}
}
