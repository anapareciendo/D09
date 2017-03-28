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
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.CustomerService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Actor;
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


	private List<Actor> actors;
	
	/* *---- List the messages that he or she’s got and reply to them-----*
	  -El orden de los parametros es: usuario que se va a autenticar,texto del mensaje, error 
	  esperado
	  
	  Cobertura del test:
			//El usuario autenticado es el dueño del mensaje a responder(test positivo)
			//El usuario no está autenticado(test negativo)
	 */
	
	@Before
    public void setup() {
		this.actors= new ArrayList<Actor>();
		this.actors.addAll(this.customerService.findAll());
		this.actors.addAll(this.adminService.findAll());
		
		Collections.shuffle(this.actors);
	}
	
	@Test
	public void driver() {
		
		final Object testingData[][] = {
			{
				actors.get(0).getUserAccount().getUsername(), "Texto", null
			//El usuario autenticado es el dueño del mensaje a responder
			}, 
			{
				null, "Texto", IllegalArgumentException.class
			//No esta autenticado
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
