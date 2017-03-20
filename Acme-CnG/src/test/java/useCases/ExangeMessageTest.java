
package useCases;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
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
public class ExangeMessageTest extends AbstractTest {

	@Autowired
	private AdministratorService	adminService;
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private MessageService			messageService;


	@Test
	public void driver() {
		final Object testingData[][] = {
			//Usuario autenticado y existe el recipient
			{
				30, Calendar.getInstance().getTime(), "title", "text", "customer1", null
			},
			//Usuario autenticado y no existe el recipient
			{
				67, Calendar.getInstance().getTime(), "title", "text", "customer1", IllegalArgumentException.class
			},
			//Usuario no autenticado y existe el recipient
			{
				30, Calendar.getInstance().getTime(), "title", "text", "noUser", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((int) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void template(final int uaId, final Date moment, final String title, final String text, final String user, final Class<?> expected) {

		Class<?> caught;
		caught = null;
		try {
			this.authenticate(user);

			Actor r = this.customerService.findByUserAccountId(uaId);
			if (r == null)
				r = this.adminService.findByUserAccountId(uaId);
			Actor s = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (s == null)
				s = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());

			final Message save = this.messageService.create(s, r);

			save.setMoment(moment);
			save.setTitle(title);
			save.setText(text);

			this.messageService.save(save);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
