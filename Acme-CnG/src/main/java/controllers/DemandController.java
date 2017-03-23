package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.DemandService;
import domain.Demand;

@Controller
@RequestMapping("/demand")
public class DemandController extends AbstractController{

	@Autowired
	private DemandService demandService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Demand> demands;
		
		demands = demandService.findAll();
		result = new ModelAndView("demand/list");
		result.addObject("requestURI", "demand/list.do");
		result.addObject("demand", demands);

		return result;
	}
}
