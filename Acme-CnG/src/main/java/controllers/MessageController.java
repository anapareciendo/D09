package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdministratorService;
import services.CustomerService;
import services.MessageService;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController{

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AdministratorService adminService;

	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Message> messages;
		
		messages = messageService.findMyMessages();
		result = new ModelAndView("message/list");
		result.addObject("requestURI", "message/list.do");
		result.addObject("ms", messages);

		return result;
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView sendMessage() {
		ModelAndView result;
		
		int uaId=LoginService.getPrincipal().getId();
		Actor actor = adminService.findByUserAccountId(uaId);
		if(actor == null){
			actor = customerService.findByUserAccountId(uaId);
		}
		
		Message ms = messageService.create(actor, actor);
		Collection<Actor> actors = new ArrayList<Actor>();
		actors.addAll(adminService.findAll());
		actors.addAll(customerService.findAll());

		result = new ModelAndView("message/send");
		result.addObject("ms", ms);
		result.addObject("actors", actors);

		return result;
	}
	
	@RequestMapping(value = "/sendMessages", method = RequestMethod.POST, params = "save")
	public ModelAndView sendMessages(@Valid Message ms, BindingResult binding) {
		ModelAndView result;
		Message res = messageService.reconstruct(ms, binding);
		if(!binding.hasErrors()){
			try{	
				messageService.save(res);
				result = new ModelAndView("redirect:list.do");

					 
			} catch (Throwable oops) {
					
				Collection<Actor> actors = new ArrayList<Actor>();
				actors.addAll(adminService.findAll());
				actors.addAll(customerService.findAll());
				result = new ModelAndView("message/send");
				result.addObject("ms", ms);
				result.addObject("actors",actors);
				result.addObject("message", "message.send.error");
					
				}
		}else{
			Collection<Actor> actors = new ArrayList<Actor>();
			actors.addAll(adminService.findAll());
			actors.addAll(customerService.findAll());
			result = new ModelAndView("message/send");
			result.addObject("ms", ms);
			result.addObject("actors",actors);
		}
		
		return result;
	}
	
}
