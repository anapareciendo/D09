/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package useCases;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.CustomerService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Customer;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReplyMessageTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdministratorService adminService;


	//Apply for a request
	@Test
	public void driver() {
		
		List<Customer> customers= new ArrayList<Customer>();
		customers.addAll(customerService.findAll());
		
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		
		final Object testingData[][] = {
			{
				customers.get(0).getUserAccount().getUsername(), "Texto", null
			//El customer1 es el due�o del mensaje a responder
			}, 
			{
				null, "Texto", IllegalArgumentException.class
			//El admin1 no es el due�o del mensaje a responder
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],(String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void template(final String username, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			List<Message> messages = new ArrayList<Message>();
			messages.addAll(this.messageService.findMyMessages());
			if(!messages.isEmpty()){
				this.messageService.replay(messages.get(0).getId(), text);
			}
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
