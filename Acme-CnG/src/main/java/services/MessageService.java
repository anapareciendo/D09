
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Message;

@Service
@Transactional
public class MessageService {

	//Managed repository
	@Autowired
	private MessageRepository	messageRepository;


	//Supporting services
	@Autowired
	private AdministratorService administratorService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private Validator validator;
	

	//Constructors
	public MessageService() {
		super();
	}

	//Simple CRUD methods
	public Message create(final Actor sender, final Actor recipient) {
		Assert.notNull(sender);
		Assert.notNull(recipient);
		Message res;
		res = new Message();
		res.setAttachments(new ArrayList<String>());
		res.setRecipient(recipient);
		res.setSender(sender);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public Collection<Message> findAll() {
		final Collection<Message> res = this.messageRepository.findAll();
		return res;
	}

	public Message findOne(final int messageId) {
		final Message res = this.messageRepository.findOne(messageId);
		return res;
	}

	public Message save(final Message message) {
		Assert.notNull(message, "The message to save cannot be null.");

		Assert.notNull(message.getSender());
		Assert.notNull(message.getRecipient());

		Assert.isTrue(message.getMoment() != null);
		Assert.isTrue(message.getTitle() != null);
		Assert.isTrue(message.getText() != null);

		final Message res = this.messageRepository.save(message);
		res.getSender().getSenderMessages().add(res);
		res.getRecipient().getReceivedMessages().add(res);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Message message) {

		Assert.notNull(message, "The message to delete cannot be null.");
		Assert.isTrue(this.messageRepository.exists(message.getId()));

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(message.getSender().getUserAccount().equals(ua), "You are not the owner of the message");

		this.messageRepository.delete(message);
	}

	//Utility Methdos
	public Collection<Message> findMyMessages() {
		return this.messageRepository.findMyMessages(LoginService.getPrincipal().getId());
	}

	public Message forward(final int messageId, final Actor recipient) {
		final Message message = this.messageRepository.findOne(messageId);
		Assert.notNull(message, "This id is not from an message");
		
		Assert.notNull(recipient,"This id is no t from an actor");
		Assert.isTrue(LoginService.getPrincipal().equals(message.getRecipient().getUserAccount()), "You are note the owner of this message");

		final Message copy = new Message();
		copy.setRecipient(recipient);
		copy.setSender(message.getRecipient());
		copy.setText(message.getText());
		copy.setTitle(message.getTitle());
		copy.setAttachments(new ArrayList<String>());
		copy.getAttachments().addAll(message.getAttachments());
		copy.setMoment(Calendar.getInstance().getTime());

		final Message res = this.messageRepository.save(copy);
		return res;

	}
	public Message replay(final int messageId, final String text) {
		final Message message = this.messageRepository.findOne(messageId);
		Assert.notNull(message, "This id is not from an message");
		Assert.notNull(text, "The text cannot be null");
		Assert.isTrue(LoginService.getPrincipal().equals(message.getRecipient().getUserAccount()), "You are note the owner of this message");

		Message res = new Message();
		res.setRecipient(message.getSender());
		res.setSender(message.getRecipient());
		res.setText(message.getText() + "<br>RE:" + text);
		res.setTitle("RE: " + message.getTitle());
		res.setAttachments(new ArrayList<String>());
		res.getAttachments().addAll(message.getAttachments());
		res.setMoment(Calendar.getInstance().getTime());
		
		return this.messageRepository.save(res);
	}
	
	public Message reconstruct(Message ms, BindingResult binding) {
		Integer uaId = LoginService.getPrincipal().getId();
		Actor sender = customerService.findByUserAccountId(uaId);
		if(sender==null){
			sender = administratorService.findByUserAccountId(uaId);
		}
		
		Message res = this.create(sender, ms.getRecipient());
		
		res.setTitle(ms.getTitle());
		res.setText(ms.getText());
		res.setAttachments(ms.getAttachments());
		res.setRecipient(ms.getRecipient());
		
		validator.validate(res, binding);
		return res;
	}

}
