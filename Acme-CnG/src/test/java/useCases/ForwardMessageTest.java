
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
public class ForwardMessageTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdministratorService administratorService;

	/* *---- List the messages that he or she’s got and forward them.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, mensaje (en caso de ser negativo, 
	  el mensaje no existe), error esperado
	  
	  Cobertura del test:
			//Reenvía un mensaje con un actor autenticado (test positivo)
			//Reenvía un mensaje con un usuario no autenticado (test negativo)
				
	 */
	
	private List<Actor> actors;

	@Before
    public void setup() {
		this.actors = new ArrayList<Actor>();
		this.actors.addAll(customerService.findAll());
		this.actors.addAll(administratorService.findAll());
		Collections.shuffle(this.actors);
	}

	@Test
	public void driver() {
		final Object testingData[][] = {
			//Reenvio mensaje
			{
				actors.get(0).getUserAccount().getUsername(), null
			},

			//No esta autenticado
			{
				null, IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void template(final String username, final Class<?> expected) {

		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			List<Message> messages = new ArrayList<Message>();
			messages.addAll(messageService.findMyMessages());
			Collections.shuffle(messages);
			Collections.shuffle(this.actors);
			if(!messages.isEmpty() && !this.actors.isEmpty()){
				this.messageService.forward(messages.get(0).getId(),this.actors.get(0));
			}
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
