/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package servicesTest;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import services.CustomerService;
import services.OfferService;
import services.PlaceService;
import utilities.AbstractTest;
import domain.Offer;
import domain.Place;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class OfferTest extends AbstractTest {

	@Autowired
	private OfferService offerService;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CustomerService customerService;

	//Create a place
	@Test
	public void createOfferPositiveTest() {
		authenticate("customer1");
		Place origin = placeService.create();
		origin.setAddress("origin");
		placeService.save(origin);
		Place destination = placeService.create();
		destination.setAddress("destination");
		placeService.save(destination);
		Offer o = offerService.create(origin, destination);
		Assert.notNull(o.getOrigin());
		Assert.notNull(o.getMoment());
		Assert.notNull(o.getDestination());
		unauthenticate();
	}

	//Create a offer with same of place null and a correct one
	@Test
	public void drivercreate(){
		Object testingData[][] = {
				{33,34,null},
				{0,35, IllegalArgumentException.class},
				{36, 0, IllegalArgumentException.class}
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
			offerService.create(o, d);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
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
				Place o = placeService.findOne(33);
				placeService.save(o);
			
				Place d = placeService.findOne(34);
				placeService.save(d);

			Offer save = offerService.create(o, d);
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
				Place o = placeService.findOne(33);
				placeService.save(o);
			
				Place d = placeService.findOne(34);
				placeService.save(d);

			Offer save = offerService.create(o, d);
			save.setTitle("title");
			save.setDescription("description"); 
				save.setCustomer(customerService.findByUserAccountId(LoginService.getPrincipal().getId()));
			offerService.save(save);
			offerService.delete(save);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
}
