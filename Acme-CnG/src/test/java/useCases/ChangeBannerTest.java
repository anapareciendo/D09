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

import services.AdministratorService;
import services.BannerService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Banner;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChangeBannerTest extends AbstractTest {

	@Autowired
	private BannerService	bannerService;
	@Autowired
	private AdministratorService adminService;

	/* *----Change the banner that the system shows on the welcome page.-----*
	  -El orden de los parámetros es:Usuario que se va a autenticar, error esperado
	  
	  Cobertura del test tanto para aceptar applications como para denegarlas:
			//El usuario autenticado es un admin (test positivo)
			//El usuario no se autentica (test negativo)
				
	 */
	@Test
	public void driver() {
		List<Administrator> admins = new ArrayList<Administrator>();
		admins.addAll(adminService.findAll());
		final Object testingData[][] = {
			{
				admins.get(0).getUserAccount().getUsername(), null
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
			List<Banner> banners = (List<Banner>) this.bannerService.findAll();
			Banner b =  banners.get(0);
			b.setLogo("Nuevo logo");
			this.bannerService.save(b);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
