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

import security.LoginService;
import services.AdministratorService;
import services.CustomerService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ExangeMessageTest extends AbstractTest {

	
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MessageService messageService;
	

	/* *----Exchange messages with other actors.-----*
	  -El orden de los parámetros para "drive" es:Usuario que se va a autenticar, usuario receptor 
	  del mensaje, título del mensaje,texto del mensaje, error esperado
	  -El orden de los parámetros para "driveModify" es: Usuario que se va a autenticar, usuario 
	  receptor del mensaje, título del mensaje,texto del mensaje,{1-2} (Si es 1, modifico el actor
	  que envía el mensaje. Si es 2, modifico el actor que recibe el mensaje), error esperado.
	  -El orden de los parámetros para "driveCreate" es: Usuario que se va a autenticar, usuario 
	  receptor del mensaje, título del mensaje,texto del mensaje,{1-2} (Si es 1, creo el actor
	  que envía el mensaje. Si es 2, creo el actor que recibe el mensaje), error esperado.
	  
	  Cobertura del test:
	 	//Usuario autenticado y existe receptor
		//Usuario autenticado y no existe el recipient
		//Usuario no autenticado y existe el recipient
		//Usuario autenticado y existe el recipient.Titulo vacio
		//Usuario autenticado y existe el recipient.Texto vacio
		//Usuario autenticado y existe el recipient.Título nulo
		//Usuario autenticado y existe el recipient.Texto nulo
		//Usuario autenticado y existe receptor.Modifico el sender antes de enviar el mensaje
		//Usuario autenticado y existe receptor.Modifico el recipient antes de enviar el mensaje
		//Creo el usuario que va a enviar el mensaje de nuevas.
		//Creo el usuario que va a recibir el mensaje de nuevas.
				
	 */
	
	private List<Actor> actors;
	
	@Before
    public void setup() {
		this.actors =  new ArrayList<Actor>();
		this.actors.addAll(this.adminService.findAll());
		this.actors.addAll(this.customerService.findAll());
		Collections.shuffle(this.actors);
		
		Collections.shuffle(this.actors);
	}
	
	@Test
	public void driver(){
		Object testingData[][] = {
				
				{
					actors.get(0).getUserAccount().getUsername(), this.actors.get(0).getId(), "titulo", "texto", null
				},
				//Usuario autenticado y existe receptor
				{
					actors.get(0).getUserAccount().getUsername(), -1, "titulo", "texto", IllegalArgumentException.class
				},
				//Usuario autenticado y no existe el receptor
				{
					null, this.actors.get(0).getId(), "titulo", "texto", IllegalArgumentException.class
				},
				//Usuario no autenticado y existe el receptor
				{
					actors.get(0).getUserAccount().getUsername(), this.actors.get(0).getId(), "", "texto", null
				},
				//Usuario autenticado y existe el recipients.Titulo vacio
				{
					actors.get(0).getUserAccount().getUsername(), this.actors.get(0).getId(), "titulo", "", null
				},
				//Usuario autenticado y existe el recipient.Texto vacio
				{
					actors.get(0).getUserAccount().getUsername(), this.actors.get(0).getId(), null, "texto", IllegalArgumentException.class
				},
				//Usuario autenticado y existe el recipient.Título nulo
				{
					actors.get(0).getUserAccount().getUsername(), this.actors.get(0).getId(), null, "texto", IllegalArgumentException.class
				},
				//Usuario autenticado y existe el recipient.Texto nulo
		};
		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void template(String username, int recipientId, String title, String text, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			Actor recipient = this.adminService.findOne(recipientId);
			if(recipient==null){
				recipient = customerService.findOne(recipientId);
			}
			Actor sender = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
			if(sender==null){
				sender= this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
			Message res =this.messageService.create(sender, recipient);
			res.setText(text);
			res.setTitle(title);
			this.messageService.save(res);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	
	
}