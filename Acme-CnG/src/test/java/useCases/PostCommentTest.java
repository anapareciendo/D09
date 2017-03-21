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
import services.CommentService;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Comment;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PostCommentTest extends AbstractTest {

	@Autowired
	private CommentService	commentService;
	@Autowired
	private CustomerService	customerService;


	//Apply for a request
	@Test
	public void driver() {
		final Object testingData[][] = {
			{
				"customer2", "prueba", "texto", 5, null
			//El customer2 postea comentar bien
			}, {
				"customer1", "", "", 5, null
			//El customer2 poste comentario blank pero no nonull
			}, {
				"customer1", null, "", 1, IllegalArgumentException.class
			//El customer1 postea comentario con el titulo a null
			}, {
				"customer2", "", null, 1, IllegalArgumentException.class
			//El customer1 postea comentario con el texto a null
			}, {
				null, "", "", 1, IllegalArgumentException.class
			//No esta autenticado
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void template(final String username, final String title, final String text, final int stars, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			final Customer customer = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			final Comment c = this.commentService.create(customer, customer);
			c.setText(text);
			c.setTitle(title);
			c.setStars(stars);
			this.commentService.save(c);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
