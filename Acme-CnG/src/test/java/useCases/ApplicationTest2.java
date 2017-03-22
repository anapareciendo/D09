
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
import services.ApplicationService;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Application;
import domain.Customer;
import domain.Demand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationTest2 extends AbstractTest {

	@Autowired
	private ApplicationService	applicationService;
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private CustomerService customerService;
	
	/* *----Demands must be accepted by the customer who posted it.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, demandas (pueden ser del
	  usuario o no, depende de lo que se vaya a testear), error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
	  		//El usuario autenticado es el dueño de la demand a la que se refiere la application
			//El usuario autenticado no es el dueño de la demand a la que se refiere la application
			//El usuario autenticado no es un customer, por lo que no puede aceptar applications
			//El usuario no esta logado 	
	 */
	

	@SuppressWarnings("unchecked")
	@Test
	public void driver() {
		
		
		
		List<Customer> customers= new ArrayList<Customer>();
		customers.addAll(customerService.findAll());
		
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		
		
		
		final Object testingDataAccept[][] = {
				
			{
				customers.get(1).getUserAccount().getUsername(), customers.get(1).getDemands(), null
		  		//El usuario autenticado es el dueño de la demand a la que se refiere la application
			}, 
			{
				customers.get(0).getUserAccount().getUsername(), customers.get(1).getDemands(), IllegalArgumentException.class
				//El usuario autenticado no es el dueño de la demand a la que se refiere la application
			},
			{
				admins.get(0).getUserAccount().getUsername(),customers.get(1).getDemands(), IllegalArgumentException.class
				//El usuario autenticado no es un customer, por lo que no puede aceptar applications
			}, 
			{
				null,customers.get(1).getDemands(), IllegalArgumentException.class
				//El usuario no esta logado 
			}, 
		};
		
		final Object testingDataDeny[][] = {
				{
					customers.get(1).getUserAccount().getUsername(), customers.get(1).getDemands(), null
			  		//El usuario autenticado es el dueño de la demand a la que se refiere la application
				}, 
				{
					customers.get(0).getUserAccount().getUsername(), customers.get(1).getDemands(), IllegalArgumentException.class
					//El usuario autenticado no es el dueño de la demand a la que se refiere la application
				},
				{
					admins.get(0).getUserAccount().getUsername(),customers.get(1).getDemands(), IllegalArgumentException.class
					//El usuario autenticado no es un customer, por lo que no puede aceptar applications
				}, 
				{
					null,customers.get(1).getDemands(), IllegalArgumentException.class
					//El usuario no esta logado 	
				}, 
			};

		

		for (int i = 0; i < testingDataAccept.length; i++)
			this.templateAccept((String) testingDataAccept[i][0], (List<Demand>) testingDataAccept[i][1], (Class<?>) testingDataAccept[i][2]);
		for (int i = 0; i < testingDataDeny.length; i++)
			this.templateDeny((String) testingDataDeny[i][0], (List<Demand>) testingDataDeny[i][1], (Class<?>) testingDataDeny[i][2]);
	}
	protected void templateAccept(final String username, List<Demand> demands, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			Demand d= demands.get(1);
			List<Application> applications = (List<Application>) d.getApplications();
			Application a = applications.get(0);
			this.applicationService.accept(a.getId());
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateDeny(final String username, List<Demand> demands, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			Demand d= demands.get(1);
			List<Application> applications = (List<Application>) d.getApplications();
			Application a = applications.get(0);
			this.applicationService.deny(a.getId());
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
