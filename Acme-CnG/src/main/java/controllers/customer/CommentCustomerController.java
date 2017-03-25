package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CommentService;
import services.CustomerService;
import services.DemandService;
import controllers.AbstractController;
import domain.Comment;
import domain.Customer;
import domain.Demand;

@Controller
@RequestMapping("/comment/customer")
public class CommentCustomerController extends AbstractController{

	@Autowired
	private DemandService demandService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public ModelAndView request(@RequestParam int demandId) {
		ModelAndView result;
		
		Customer sender = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		Comment res = commentService.create(sender, demandService.findOne(demandId));
		
		result = new ModelAndView("demand/post");
		result.addObject("comment", res);

		return result;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView search(Comment comment, BindingResult binding) {
		ModelAndView result;
		try{
		Comment res = commentService.reconstruct(comment, binding);
			try{	
				commentService.save(res);
				Demand demand = demandService.findOne(res.getCommentable().getId());
				Collection<Comment> comments = demand.getComments();
				result = new ModelAndView("demand/display");
				result.addObject("demand", demand);
				result.addObject("comments", comments);

			} catch (Throwable oops) {
				result = new ModelAndView("demand/post");
				result.addObject("comment", comment);
				result.addObject("message", "message.send.error");
					
				}
		}catch(Throwable opps){
			result = new ModelAndView("demand/post");
			result.addObject("comment", comment);
		}
		
		return result;
	}
}
