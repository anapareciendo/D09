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

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.PlaceService;
import utilities.AbstractTest;
import domain.Place;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PlaceTest extends AbstractTest {

	@Autowired
	private PlaceService placeService;


	//Save a place without address.
	@Test(expected = IllegalArgumentException.class)
	public void savePlaceNegativeTest2() {
		authenticate("customer3");
		Place p = placeService.create();
		//p.setAddress("Address cambiado");
		placeService.save(p);
		unauthenticate();
	}
	
	//Save a place with necesary attributes, not with de optional one
	//Save a place with a admin. Places cannot be create by them.
	@Test
	public void driver(){
		Object testingData[][] = {
				{"customer1",null},
				{"admin1",IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void template(String username, Class<?> expected){
		Class<?> caught;
		caught=null;
		try{
			authenticate(username);
			Place p = placeService.create();
			p.setAddress("Address cambiado");
			placeService.save(p);
			unauthenticate();
		}catch(Throwable oops){
			caught =  oops.getClass();
		}
		checkExceptions(expected, caught);
	}


}
