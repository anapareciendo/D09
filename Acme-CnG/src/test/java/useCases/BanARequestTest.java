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

import services.AdministratorService;
import services.CustomerService;
import services.DemandService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Customer;
import domain.Demand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BanARequestTest extends AbstractTest {

	
	@Autowired
	private DemandService demandService;
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private CustomerService customerService;

	

	/* *----Ban a request that he or she finds inappropriate.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
			//El usuario autenticado es un admin (test positivo)
			//El usuario no se autentica (test negativo)
				
	 */
	
	private List<Customer> customers;
	private List<Administrator> admins;
	
	@Before
    public void setup() {
		this.customers= new ArrayList<Customer>();
		this.customers.addAll(customerService.findAll());
		
		this.admins = new ArrayList<Administrator>();
		this.admins.addAll(adminService.findAll());
		
		Collections.shuffle(admins);
		Collections.shuffle(customers);
	}

	@Test
	public void driver(){
		
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		
		Object testingData[][] = {
				{admins.get(0).getUserAccount().getUsername(),null},
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
			List<Demand> demands = new ArrayList<Demand>();
			demands.addAll(demandService.findNoBannedRequests());
			Collections.shuffle(demands);
			if(!demands.isEmpty()){
				Demand re= demands.get(0);
				demandService.ban(re.getId());
			}
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
}
