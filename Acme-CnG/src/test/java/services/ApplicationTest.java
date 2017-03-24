/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Application;
import domain.Customer;
import domain.Demand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationTest extends AbstractTest {

	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DemandService offerService;



	//Create an application well.
	//Create an application with some params wrong
	@Test
	public void drivercreate(){
		Object testingData[][] = {
				{33,41,null},
				{0,42, IllegalArgumentException.class},
				{34, 0, IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templatecreate((int) testingData[i][0],
					(int) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templatecreate(int customer, int demand, Class<?> expected){
		Class<?> caught;
		Customer c=null;
		Demand d= null;
		caught = null;
		try{
			authenticate("customer2");
			if (customer!=0){
				c = customerService.findOne(customer);
				customerService.save(c);
			}
			if(demand!=0){
				d = offerService.findOne(demand);
				offerService.save(d);
			}
			applicationService.create(c, d);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

	//Save an application with the correct user login
	//Save an application with the wrong user login
	@Test
	public void driversave(){
		Object testingData[][] = {
				{"customer1",null},
				{"admin1",IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			templatesave((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templatesave( String username,Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
				Customer c = customerService.findOne(33);
			
				Demand o = offerService.findOne(41);

			Application save = applicationService.create(c, o);
			
			applicationService.save(save);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	

	
	
	
}
