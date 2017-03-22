
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
		Assert.isTrue(message.getTitle() != null && message.getTitle() != "");
		Assert.isTrue(message.getText() != null && message.getText() != "");

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

	public Message copy(final int messageId) {
		final Message message = this.messageRepository.findOne(messageId);
		Assert.notNull(message, "This id is not from an message");

		Assert.isTrue(LoginService.getPrincipal().equals(message.getSender().getUserAccount()), "You are note the owner of this message");

		final Message copy = new Message();
		copy.setRecipient(message.getRecipient());
		copy.setSender(message.getSender());
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
		Assert.isTrue(LoginService.getPrincipal().equals(message.getSender().getUserAccount()), "You are note the owner of this message");

		message.setText(message.getText() + "\n\nReply:\n" + text);
		message.setTitle("RE:" + message.getTitle());

		final Message res = this.messageRepository.save(message);
		return res;

	}
	
	public Message reconstruct(Message ms, BindingResult binding) {
		Message res;
		
		if(ms.getId()==0){
			res = this.create(ms.getRecipient(), ms.getRecipient());
		}else{
			Message aux = messageRepository.findOne(ms.getId());
			messageRepository.delete(aux);
			res= this.create(aux.getSender(), aux.getRecipient());
		}
		res.setTitle(ms.getTitle());
		res.setText(ms.getText());
		res.setAttachments(ms.getAttachments());
		res.setRecipient(ms.getRecipient());
		
		validator.validate(res, binding);
		return res;
	}

}
