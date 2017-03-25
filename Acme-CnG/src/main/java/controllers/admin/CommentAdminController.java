package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.DemandService;
import controllers.AbstractController;
import domain.Demand;

@Controller
@RequestMapping("/comment/admin")
public class CommentAdminController extends AbstractController{

	@Autowired
	private CommentService commentService;
	@Autowired
	private DemandService demandService;
	
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam int commentId) {
		ModelAndView result;
	
		commentService.ban(commentId);
		
		Collection<Demand> demands = demandService.findAll();
		result = new ModelAndView("demand/list");
		result.addObject("requestURI", "demand/admin/list.do");
		result.addObject("demand", demands);
		result.addObject("message", "demand.comment.ban.ok");
		
		return result;
	}
}
