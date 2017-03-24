package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DemandService;
import controllers.AbstractController;
import domain.Demand;

@Controller
@RequestMapping("/demand/admin")
public class DemandAdminController extends AbstractController{

	@Autowired
	private DemandService demandService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Demand> demands = demandService.findAll();
		result = new ModelAndView("demand/list");
		result.addObject("requestURI", "demand/admin/list.do");
		result.addObject("demand", demands);

		return result;
	}
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int demandId) {
		ModelAndView result;
	
		demandService.ban(demandId);
		
		result = new ModelAndView("redirect:list.do");

		return result;
	}
}
