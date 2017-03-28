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
import services.CommentService;
import services.DemandService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Comment;
import domain.Demand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BanACommentTest extends AbstractTest {

	@Autowired
	private CommentService	commentService;
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private DemandService demandService;

	private List<Administrator> admins;
	
	/* *----Ban a comment that administrator finds inappropriate.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
	  		//El usuario autenticado es un admin (test positivo)
			//El usuario no está autenticado  (test negativo)
				
	 */
	
	@Before
    public void setup() {
		this.admins = new ArrayList<Administrator>();
		this.admins.addAll(adminService.findAll());
		
		Collections.shuffle(this.admins);
	}
	
	@Test
	public void driver() {
		final Object testingData[][] = {
			{
				this.admins.get(0).getUserAccount().getUsername(), null
			}, {
				null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void template(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			List<Comment> comments = new ArrayList<Comment>();
			List<Demand> demands = new ArrayList<Demand>();
			demands.addAll(demandService.findNoBannedOffers());
			demands.addAll(demandService.findNoBannedRequests());
			Collections.shuffle(demands);
			
			if(!demands.isEmpty()){
				comments.addAll(this.commentService.findRealComments(demands.get(0).getId()));
				if(!comments.isEmpty()){
					Collections.shuffle(comments);
					this.commentService.ban(comments.get(0).getId());
				}
			}
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
