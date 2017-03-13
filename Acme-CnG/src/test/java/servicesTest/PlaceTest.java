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

	//Create a place with necesary attributes, not with de optional one
	@Test
	public void createPlacePositiveTest() {
		authenticate("customer1");
		Place p = placeService.create();
		p.setAddress("Address cambiado");
		placeService.save(p);
	}

	//Create a place with a admin. Places cannot be create by them.
	@Test(expected = IllegalArgumentException.class)
	public void createPlaceNegativeTest() {
		authenticate("admin1");
		Place p = placeService.create();
		p.setAddress("Address cambiado");
		placeService.save(p);
	}

	
	// Ancillary methods ------------------------------------------------------

}
