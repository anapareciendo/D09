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
import domain.Actor;
import domain.Comment;
import domain.Commentable;
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
	public ModelAndView request(@RequestParam int commentableId) {
		ModelAndView result;
		
		Customer sender = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		
		Commentable commentable = demandService.findOne(commentableId);
		if(commentable==null){
			commentable = customerService.findOne(commentableId);
		}
		
		Comment res = commentService.create(sender, commentable);
		
		result = new ModelAndView("comment/post");
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
				Commentable commentable = demandService.findOne(res.getCommentable().getId());
				if(commentable == null){
					commentable = customerService.findOne(res.getCommentable().getId());
				}
				Collection<Comment> comments = commentService.findRealComments(commentable.getId());
				
				if(commentable instanceof Demand){
					result = new ModelAndView("demand/display");
					Demand demand = (Demand) commentable;
					result.addObject("demand", demand);
				}else{
					result = new ModelAndView("customer/display");
					Actor actor = (Actor) commentable;
					result.addObject("actor", actor);
				}
				result.addObject("comments", comments);

			} catch (Throwable oops) {
				result = new ModelAndView("comment/post");
				result.addObject("comment", comment);
				result.addObject("message", "message.send.error");
			}
		}catch(Throwable opps){
			result = new ModelAndView("comment/post");
			result.addObject("comment", comment);
			result.addObject("message", "message.send.error");
		}
		
		return result;
	}
}
