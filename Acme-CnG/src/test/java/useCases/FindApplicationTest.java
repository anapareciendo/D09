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

import services.ApplicationService;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FindApplicationTest extends AbstractTest {

	@Autowired
	private ApplicationService appService;
	@Autowired
	private CustomerService customerService;

	
	/* *----. Applications can be pending, accepted, or denied..-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
			//El usuario autenticado es un customer (test positivo)
			//El usuario autenticado es un admin (test negativo)
				
	 */
	
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
	
	protected void template(String username,Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			appService.findApplicationMyOffers();
			appService.findApplicationMyRequests();
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
}
