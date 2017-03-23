
package useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CustomerService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Customer;
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

	/* *---- List the messages that he or she’s got and forward them.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, mensaje (en caso de ser negativo, 
	  el mensaje no existe), error esperado
	  
	  Cobertura del test:
			//Reenvía un mensaje con un customer autenticado (test positivo)
			//Reenvía un mensaje que no es valido (test negativo)
				
	 */

	@Test
	public void driver() {
		
		List<Customer> customers= new ArrayList<Customer>();
		customers.addAll(customerService.findAll());
		
		
		
		final Object testingData[][] = {
			//Reenvio mensaje
			{
				customers.get(0).getUserAccount().getUsername(), null
			},

			//Id erronea
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
			if(!messages.isEmpty()){
				this.messageService.copy(messages.get(0).getId());
			}
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
