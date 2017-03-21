
package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.MessageService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ForwardMessageTest extends AbstractTest {

	@Autowired
	private MessageService	messageService;


	@Test
	public void driver() {
		final Object testingData[][] = {
			//Reenvio mensaje
			{
				"customer1", 35, null
			},

			//Id erronea
			{
				"customer1", 42, IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void template(final String username, final int id, final Class<?> expected) {

		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);

			this.messageService.copy(id);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
