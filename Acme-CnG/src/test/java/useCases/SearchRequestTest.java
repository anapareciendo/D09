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

import services.DemandService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SearchRequestTest extends AbstractTest {

	@Autowired
	private DemandService demandService;

	//Search for Request
	@Test
	public void driver(){
		Object testingData[][] = {
				{"customer1","want",null},
				{"admin1","want", IllegalArgumentException.class},
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
			demandService.searchRequest(keyword);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	
}
