package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CustomerService;
import services.DemandService;
import services.PlaceService;
import domain.Customer;
import domain.Demand;
import domain.Place;

@Controller
@Transactional
@RequestMapping("/request")
public class RequestController extends AbstractController{

	@Autowired
	private DemandService demandService;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private CustomerService customerService;
	
	public RequestController() {
		super();
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		List<Place> places = new ArrayList<Place>();
		places.addAll(placeService.findAll());
		
		result = new ModelAndView("request/create");
		
		if(places.isEmpty()){
			result.addObject("vacio", true);
		}else{
			Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			Demand request = demandService.createRequest(places.get(0), places.get(0), c);
			result.addObject("request", request);
			result.addObject("places",places);
		}
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Demand request, BindingResult binding) {
		ModelAndView result;
		
		Demand res = demandService.reconstructRequest(request, binding);
		if(!binding.hasErrors()){
			try{	
				demandService.save(res);
				result = new ModelAndView("welcome/index");
				result.addObject("message","request.all.green");
					 
			} catch (Throwable oops) {
					
				Collection<Place> places = placeService.findAll();
				result = new ModelAndView("request/create");
				result.addObject("request", request);
				result.addObject("places",places);
				result.addObject("message", "request.commit.error");
					
				}
		}else{
			Collection<Place> places = placeService.findAll();
			result = new ModelAndView("request/create");
			result.addObject("request", request);
			result.addObject("places",places);
//			result.addObject("errors", binding.getAllErrors());
		}
			
		return result;
	}
	
}

