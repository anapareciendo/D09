
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
import domain.Administrator;
import domain.Customer;
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
			//Usuario autenticado y existe el recipient. Primero creo el mensaje, luego modifico al usuario y luego envío.
			{
				28, Calendar.getInstance().getTime(), "title", "text", "customer1", null
			},
			//Usuario autenticado y existe el recipient.Titulo vacio
			{
				30, Calendar.getInstance().getTime(), "", "text", "customer1", IllegalArgumentException.class
			},
			//Usuario autenticado y existe el recipient.Texto vacio
			{
				30, Calendar.getInstance().getTime(), "title", "", "customer1", IllegalArgumentException.class
			},
			//Usuario autenticado y existe el recipient.Texto nulo
			{
				30, Calendar.getInstance().getTime(), "title", null, "customer1", IllegalArgumentException.class
			},
			//Usuario autenticado y existe el recipient.Título nulo
			{
				30, Calendar.getInstance().getTime(), null, "text", "customer1", IllegalArgumentException.class
			}

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

			//Primero modifico el sender y luego creo el mensaje
			if (uaId == 27) {
				final Administrator admin = this.adminService.findByUserAccountId(uaId);
				admin.setName("Nuevo nombre");
				this.adminService.save(admin);
			}
			final Message save = this.messageService.create(s, r);

			//Creo el mensaje y luego modifico el sender
			if (uaId == 28) {
				final Customer c = this.customerService.findByUserAccountId(uaId);
				c.setName("Nuevo nombre");
				this.customerService.save(c);
			}
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

	//	@Test
	//	public void driverCreate() {
	//		final Object testingData[][] = {
	//			//Creo un actor, me autentico y envio un mensaje escrito por el
	//			{
	//				30, Calendar.getInstance().getTime(), "title", "text", "customer1", null
	//			},
	//			//Envio un mensaje a un actor recien creado
	//			{
	//				100, Calendar.getInstance().getTime(), "title", "text", "customer1", null
	//			}
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateCreate((int) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	//	}
	//
	//	protected void templateCreate(final int uaId, final Date moment, final String title, final String text, final String user, final Class<?> expected) {
	//
	//		Class<?> caught;
	//		caught = null;
	//		try {
	//
	//			if (user == null) {
	//				final Collection<Authority> authorities = new ArrayList<Authority>();
	//				final Authority a = new Authority();
	//				a.setAuthority(Authority.CUSTOMER);
	//				authorities.add(a);
	//				final UserAccount ua = new UserAccount();
	//				ua.setUsername("username");
	//				ua.setPassword("password");
	//				ua.setAuthorities(authorities);
	//
	//				final Customer c = this.customerService.create(ua);
	//				c.setName("name");
	//				c.setSurname("surname");
	//				c.setEmail("email@hola.com");
	//				c.setPhone("+34122332687");
	//
	//				this.customerService.save(c);
	//				this.authenticate("username");
	//				final Customer sender = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
	//				Actor r = this.customerService.findByUserAccountId(uaId);
	//				if (r == null)
	//					r = this.adminService.findByUserAccountId(uaId);
	//
	//				final Message save = this.messageService.create(sender, r);
	//				save.setMoment(moment);
	//				save.setTitle(title);
	//				save.setText(text);
	//
	//				this.messageService.save(save);
	//				this.unauthenticate();
	//
	//			} else if (uaId == 100) {
	//				final Collection<Authority> authorities = new ArrayList<Authority>();
	//				final Authority a = new Authority();
	//				a.setAuthority(Authority.CUSTOMER);
	//				authorities.add(a);
	//				final UserAccount ua = new UserAccount();
	//				ua.setUsername("username");
	//				ua.setPassword("password");
	//				ua.setAuthorities(authorities);
	//
	//				final Customer c = this.customerService.create(ua);
	//				c.setName("name");
	//				c.setSurname("surname");
	//				c.setEmail("email@hola.com");
	//				c.setPhone("+34122332687");
	//
	//				this.customerService.save(c);
	//
	//				this.authenticate(user);
	//
	//				final Actor s = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
	//
	//				final Message save = this.messageService.create(s, c);
	//				save.setMoment(moment);
	//				save.setTitle(title);
	//				save.setText(text);
	//
	//				this.messageService.save(save);
	//				this.unauthenticate();
	//			}
	//
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//		this.checkExceptions(expected, caught);
	//	}
}
