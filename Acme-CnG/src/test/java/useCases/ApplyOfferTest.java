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

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.ApplicationService;
import services.CustomerService;
import services.Demand2Service;
import utilities.AbstractTest;
import domain.Application;
import domain.Customer;
import domain.Offer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplyOfferTest extends AbstractTest {

	@Autowired
	private ApplicationService appService;
	@Autowired
	private Demand2Service offerService;
	@Autowired
	private CustomerService customerService;

	//Apply for a request
	@Test
	public void driver(){
		Object testingData[][] = {
				{"customer1",null},
				{"admin1",IllegalArgumentException.class},
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
			offerService.findNoBannedOffers();
			Offer re= offerService.findOne(41);
			Customer cus= customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			Application ap= appService.create(cus, re);
			appService.save(ap);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
}
