package controllers.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DemandService;
import controllers.AbstractController;
import domain.Demand;
import domain.Type;

@Controller
@RequestMapping("/demand/customer")
public class DemandCustomerController extends AbstractController{

	@Autowired
	private DemandService demandService;
	
	@RequestMapping(value = "/offer", method = RequestMethod.GET)
	public ModelAndView offer() {
		ModelAndView result;
		List<Demand> demands = new ArrayList<Demand>();
		
		demands.addAll(demandService.findNoBannedOffers());
		
		result = new ModelAndView("demand/list");
		result.addObject("requestURI", "demand/customer/offer.do");
		result.addObject("demand", demands);

		return result;
	}
	
	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public ModelAndView request() {
		ModelAndView result;
		List<Demand> demands = new ArrayList<Demand>();
		
		demands.addAll(demandService.findNoBannedRequests());
		
		result = new ModelAndView("demand/list");
		result.addObject("requestURI", "demand/customer/request.do");
		result.addObject("demand", demands);

		return result;
	}
	
	@RequestMapping(value = "/apply", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int demandId) {
		ModelAndView result;
	
		Type type =demandService.apply(demandId);
		
		if(type==Type.OFFER){
			result = new ModelAndView("redirect:offer.do");
		}else{
			result = new ModelAndView("redirect:request.do");
		}
		return result;
	}
}
