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

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.Demand2Service;
import services.PlaceService;
import utilities.AbstractTest;
import domain.Customer;
import domain.Offer;
import domain.Place;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class OfferTest extends AbstractTest {

	@Autowired
	private Demand2Service offerService;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CustomerService customerService;



	//Create a offer with some of place null and a correct one
	@Test
	public void drivercreate(){
		Object testingData[][] = {
				{37,38,null},
				{0,40, IllegalArgumentException.class},
				{39, 0, IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templatecreate((int) testingData[i][0],
					(int) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templatecreate(int origin, int destination, Class<?> expected){
		Class<?> caught;
		Place o=null;
		Place d= null;
		caught = null;
		try{
			authenticate("customer2");
			if (origin!=0){
				o = placeService.findOne(origin);
				placeService.save(o);
			}
			if(destination!=0){
				d = placeService.findOne(destination);
				placeService.save(d);
			}
			Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			offerService.create(o, d, c);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	//Save an offer with all its attributes well
	//Save an offer with some of its attributes wrong
	@Test
	public void driversave(){
		Object testingData[][] = {
				{"title","description",Calendar.getInstance().getTime(),Boolean.FALSE,null},
				{"","description",Calendar.getInstance().getTime(),Boolean.FALSE,IllegalArgumentException.class},
				{"title","",Calendar.getInstance().getTime(),Boolean.FALSE,IllegalArgumentException.class},
				{"title","description",null,Boolean.FALSE,IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			templatesave((String) testingData[i][0],
					(String) testingData[i][1],
					(Date) testingData[i][2],
					(boolean) testingData[i][3],
					(Class<?>) testingData[i][4]);
		}
	}
	
	protected void templatesave(String title, String description,Date moment,boolean banned, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate("customer2");
				Place o = placeService.findOne(37);
				placeService.save(o);
			
				Place d = placeService.findOne(38);
				placeService.save(d);
			Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			Offer save = offerService.create(o, d, c);
			save.setTitle(title);
			save.setDescription(description);
			save.setMoment(moment);
			save.setBanned(banned);
			
			offerService.save(save);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	//Delete an offer with the correct user login
	//Delete an offer with the wrong user login
	@Test
	public void driverdelete(){
		Object testingData[][] = {
				{"customer1",null},
				{"admin1",IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			templatedelete((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templatedelete(String username, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			Offer of= offerService.findOne(41);
			offerService.delete(of);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
}
