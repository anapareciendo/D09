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

import services.CommentService;
import services.DemandService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DisplayRequestTest extends AbstractTest {

	@Autowired
	private CommentService	commentService;
	@Autowired
	private DemandService	requestService;


	//Apply for a request
	@Test
	public void driver() {
		final Object testingData[][] = {
			{
				"customer2", 44, null
			//Esta autenticado como customer
			}, {
				"admin1", 44, null
			//Esta autenticado como admin
			}, {
				null, 41, IllegalArgumentException.class
			//no esta autenticado
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void template(final String username, final int id, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			this.requestService.findOne(id);
			this.commentService.findReceivedComments(id);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
