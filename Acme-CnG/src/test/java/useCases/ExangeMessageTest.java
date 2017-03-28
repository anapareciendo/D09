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
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.AdministratorService;
import services.CustomerService;
import services.MessageService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Customer;
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
	 
	  Cobertura del test:*/ 
	 	//Usuario autenticado y existe receptor
		//Usuario autenticado y no existe el recipient
		//Usuario no autenticado
		//Usuario autenticado y existe el recipient.Titulo vacio
		//Usuario autenticado y existe el recipient.Texto vacio
		//Usuario autenticado y existe el recipient.Título nulo
		//Usuario autenticado y existe el recipient.Texto nulo
		
		//Registrarse como Customer y enviar un mensaje
		//Registrarse como Customer con el nombre a null
		//Registrarse como Customer con el patron del telefono erroneo
	 
	
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
		Object testingDataNew[][]={
				//Customer entra al sistema y envia un mensaje
				{"customerTest","password","Aloy","Ramos","aloyR@gmail.com","+34122332687", null},
//				//Customer se intenta registrar con el campo nombre a null y luego mandar un mensaje
				{"customerTest2","password",null,"Ramos","aloyR@gmail.com","+34122332687", IllegalArgumentException.class},
//				//Customer se intenta registrar incumpliendo el patron del telefono y luego mandar un mensaje
				{"customerTest3","password","Aloy","Ramos","aloyR@gmail.com","32687", ConstraintViolationException.class},
		};
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
		for (int i = 0; i < testingDataNew.length; i++)
			this.templateNew((String) testingDataNew[i][0], (String) testingDataNew[i][1],(String) testingDataNew[i][2],(String) testingDataNew[i][3],(String) testingDataNew[i][4],(String) testingDataNew[i][5], (Class<?>) testingDataNew[i][6]);
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
	
	private void templateNew(String username, String password, String name, String surname, String email, String phone, Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			UserAccount ua = new UserAccount();
			Authority a = new Authority();
			a.setAuthority(Authority.CUSTOMER);
			ua.setAuthorities(new ArrayList<Authority>());
			ua.setPassword(password);
			ua.setUsername(username);
			ua.getAuthorities().add(a);
			
			Customer c = this.customerService.create(ua);
			c.setName(name);
			c.setSurname(surname);
			c.setEmail(email);
			c.setPhone(phone);
			
			Customer s =this.customerService.save(c);
			
			
			this.authenticate(c.getUserAccount().getUsername());
			Actor recipient = this.actors.get(0);
			
			Message res =this.messageService.create(s, recipient);
			res.setText("Test");
			res.setTitle("Test");
			
			this.messageService.save(res);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
	
}