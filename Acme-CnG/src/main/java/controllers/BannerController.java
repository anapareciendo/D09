package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Banner;

import services.BannerService;

@Controller
@RequestMapping("/banner")
public class BannerController extends AbstractController{

	@Autowired
	private BannerService bannerService;
	
	public BannerController() {
		super();
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		
		try{
			Banner banner;
			List<Banner> banners = new ArrayList<Banner>(bannerService.findAll());
			banner = banners.get(0);
			result = new ModelAndView("banner/edit");
			result.addObject("banner", banner);
		}catch(Throwable oops){
			result = new ModelAndView("welcome/index");
			result.addObject("message","banner.commit.error");

		}
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Banner banner, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("banner/edit");
			result.addObject("banner", banner);
		} else {
			try {
				bannerService.save(banner);				
				result = new ModelAndView("banner/edit");
				result.addObject("banner", banner);
				result.addObject("message","banner.commit.ok");
			} catch (Throwable oops) {
				result = new ModelAndView("banner/edit");
				result.addObject("banner", banner);		
				result.addObject("message", "banner.commit.error");
			}
		}

		return result;
	}
}
