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
import services.OfferService;
import services.PlaceService;
import domain.Customer;
import domain.Offer;
import domain.Place;

@Controller
@Transactional
@RequestMapping("/offer")
public class OfferController extends AbstractController{

	@Autowired
	private OfferService offerService;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private CustomerService customerService;
	
	public OfferController() {
		super();
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		List<Place> places = new ArrayList<Place>();
		places.addAll(placeService.findAll());
		
		result = new ModelAndView("offer/create");
		
		if(places.isEmpty()){
			result.addObject("vacio", true);
		}else{
			Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			Offer offer = offerService.create(places.get(0), places.get(0), c);
			result.addObject("offer", offer);
			result.addObject("places",places);
		}
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Offer offer, BindingResult binding) {
		ModelAndView result;
		
		Offer res = offerService.reconstruct(offer, binding);
		if(!binding.hasErrors()){
			try{	
				offerService.save(res);
				result = new ModelAndView("welcome/index");
				result.addObject("message","offer.all.green");
					 
			} catch (Throwable oops) {
					
				Collection<Place> places = placeService.findAll();
				result = new ModelAndView("offer/create");
				result.addObject("offer", offer);
				result.addObject("places",places);
				result.addObject("message", "offer.commit.error");
					
				}
		}else{
			Collection<Place> places = placeService.findAll();
			result = new ModelAndView("offer/create");
			result.addObject("offer", offer);
			result.addObject("places",places);
//			result.addObject("errors", binding.getAllErrors());
		}
			
		return result;
	}
	
}

