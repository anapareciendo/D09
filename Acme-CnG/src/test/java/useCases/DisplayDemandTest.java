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
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Administrator;
import domain.Customer;
import domain.Demand;

import services.AdministratorService;
import services.CommentService;
import services.CustomerService;
import services.DemandService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DisplayDemandTest extends AbstractTest {

	@Autowired
	private CommentService	commentService;
	@Autowired
	private DemandService	demandService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdministratorService adminService;


	//
	/* *----Display demands. Incluye la demand en si y los comentarios recibidos.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar,demands del sistema, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
	  		//El usuario autenticado es un customer (test positivo)
			//El usuario autenticado es un admin (test positivo)
			//No hay usuario autenticado (test negativo)
				
	 */
	@Test
	public void driver() {
		List<Customer> customers= new ArrayList<Customer>();
		customers.addAll(customerService.findAll());
		
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		
		List<Demand> demands=new ArrayList<Demand>();
		demands.addAll(demandService.findAll());
		
		final Object testingData[][] = {
			{
				customers.get(0).getUserAccount().getUsername(), demands.get(0).getId(), null
			//Esta autenticado como customer
			}, {
				admins.get(0).getUserAccount().getUsername(), demands.get(0).getId(), null
			//Esta autenticado como admin
			}, {
				null, demands.get(0).getId(), IllegalArgumentException.class
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
			this.demandService.findOne(id);
			this.commentService.findReceivedComments(id);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
