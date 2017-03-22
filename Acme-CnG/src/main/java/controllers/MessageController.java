package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.MessageService;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController{

	@Autowired
	private MessageService messageService;
	
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
}
