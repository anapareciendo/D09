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
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Administrator;
import domain.Customer;

import services.AdministratorService;
import services.CustomerService;
import services.DemandService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SearchDemandTest extends AbstractTest {

	@Autowired
	private DemandService demandService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdministratorService adminService;

	/* *----Search demands.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
			//El usuario autenticado es un customer (test positivo)
			//El usuario autenticado es un admin (test negativo)
				
	 */
	@Test
	public void driver(){
		
		List<Customer> customers= new ArrayList<Customer>();
		customers.addAll(customerService.findAll());
		
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		
		Object testingData[][] = {
				{customers.get(0).getUserAccount().getUsername(),"want",null},
				{admins.get(0).getUserAccount().getUsername(),"want", IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(String) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void template(String username, String keyword, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			demandService.searchOffers(keyword);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
}
