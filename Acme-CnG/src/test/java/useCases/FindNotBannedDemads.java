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

import services.CustomerService;
import services.DemandService;
import utilities.AbstractTest;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FindNotBannedDemads extends AbstractTest{

	//
	
	/* *----Demands banned must not be displayed to a general audience, only to the administrators and the customer who posted it.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
			//El usuario autenticado es un customer (test positivo)
			//El usuario no está autenticado (test negativo)
				
	 */
	
	@Autowired
	private DemandService demandService;
	@Autowired
	private CustomerService customerService;

	private List<Customer> customers;

	@Before
    public void setup() {
		this.customers= new ArrayList<Customer>();
		this.customers.addAll(customerService.findAll());
		
		Collections.shuffle(customers);
	}
	
	@Test
	public void driver(){
		Object testingData[][] = {
				{customers.get(0).getUserAccount().getUsername(),null},
				{null,IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void template(String user, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(user);
			demandService.findNoBannedOffers();
			demandService.findNoBannedRequests();
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
}
