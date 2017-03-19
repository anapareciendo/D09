
package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ApplicationService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationTest extends AbstractTest {

	@Autowired
	private ApplicationService	applicationService;


	@Test
	public void driver() {
		final Object testingDataAccept[][] = {
			{
				"customer2", 46, null
			//El customer2 es el due�o de la offer a la que se refiere la application con id=46
			}, {
				"customer1", 46, IllegalArgumentException.class
			//El customer1 no es el due�o del a offer a la que se refiere la application con id=46
			}, {
				"admin1", 46, IllegalArgumentException.class
			//El admin1 no es un customer, por lo que no puede aceptar applications
			}, {
				"customer2", 250, IllegalArgumentException.class
			//El id=250 no pertenece a una application
			}, {
				null, 46, IllegalArgumentException.class
			//El usuario no esta logado 
			}, {
				"customer2", 48, null
			//El customer2 es el due�o de la request a la que se refiere la application con id=46
			}, {
				"customer1", 48, IllegalArgumentException.class
			//El customer1 no es el due�o del a request a la que se refiere la application con id=46
			}, {
				"admin1", 48, IllegalArgumentException.class
			//El admin1 no es un customer, por lo que no puede aceptar applications
			},
		};

		final Object testingDataDeny[][] = {
			{
				"customer2", 46, null
			//El customer2 es el due�o de la offer a la que se refiere la application con id=46
			}, {
				"customer1", 46, IllegalArgumentException.class
			//El customer1 no es el due�o del a offer a la que se refiere la application con id=46
			}, {
				"admin1", 46, IllegalArgumentException.class
			//El admin1 no es un customer, por lo que no puede aceptar applications
			}, {
				"customer2", 250, IllegalArgumentException.class
			//El id=250 no pertenece a una application
			}, {
				null, 46, IllegalArgumentException.class
			//El usuario no esta logado
			}, {
				"customer2", 48, null
			//El customer2 es el due�o de la request a la que se refiere la application con id=46
			}, {
				"customer1", 48, IllegalArgumentException.class
			//El customer1 no es el due�o del a request a la que se refiere la application con id=46
			}, {
				"admin1", 48, IllegalArgumentException.class
			//El admin1 no es un customer, por lo que no puede aceptar applications
			},
		};

		for (int i = 0; i < testingDataAccept.length; i++)
			this.templateAccept((String) testingDataAccept[i][0], (Integer) testingDataAccept[i][1], (Class<?>) testingDataAccept[i][2]);
		for (int i = 0; i < testingDataDeny.length; i++)
			this.templateDeny((String) testingDataDeny[i][0], (Integer) testingDataDeny[i][1], (Class<?>) testingDataDeny[i][2]);
	}

	protected void templateAccept(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.applicationService.accept(id);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateDeny(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.applicationService.deny(id);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
