package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;

@Service
@Transactional
public class MessageService {

	//Managed repository
	@Autowired
	private MessageRepository messageRepository;
	
	//Supporting services

	
	/*@Autowired
	private Validator validator;*/
	
	//Constructors
	public MessageService() {
		super();
	}
	
	//Simple CRUD methods
	public Message create(Actor sender, Actor recipient) {
		Assert.notNull(sender);
		Assert.notNull(recipient);
		Message res;
		res = new Message();
		res.setRecipient(recipient);
		res.setSender(sender);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}
	
	public Collection<Message> findAll() {
		Collection<Message> res = messageRepository.findAll();
		return res;
	}

	public Message findOne(int messageId) {
		Message res = messageRepository.findOne(messageId);
		return res;
	}
	
	public Message save(Message message) {
		Assert.notNull(message, "The message to save cannot be null.");

		Message res = messageRepository.save(message);
		res.getSender().getSenderMessages().add(res);
		res.getRecipient().getReceivedMessages().add(res);

		return res;
	}
	
	
	
}
