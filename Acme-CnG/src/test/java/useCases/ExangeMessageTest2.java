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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

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
import domain.Administrator;
import domain.Customer;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ExangeMessageTest2 extends AbstractTest {

	
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
	
	@Test
	public void driver(){
		List<Actor> actors = new ArrayList<Actor>();
		actors.addAll(adminService.findAll());
		actors.addAll(customerService.findAll());
		Collections.shuffle(actors);
		
		Object testingData[][] = {
				//Usuario autenticado y existe receptor
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"titulo","text",null},
				//Usuario autenticado y no existe el recipient
				{actors.get(0).getUserAccount().getUsername(),-30,"titulo","text",IllegalArgumentException.class},
				//Usuario no autenticado y existe el recipient
				{null,actors.get(1).getId(),"titulo","text",IllegalArgumentException.class},
				//Usuario autenticado y existe el recipient.Titulo vacio
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"","text",IllegalArgumentException.class},
				//Usuario autenticado y existe el recipient.Texto vacio
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"titulo","",IllegalArgumentException.class},
				//Usuario autenticado y existe el recipient.Título nulo
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),null,"text",IllegalArgumentException.class},
				//Usuario autenticado y existe el recipient.Texto nulo
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"title",null,IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String)testingData[i][0], 
					(int) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(Class<?>) testingData[i][4]);
		}
	}
	
	
	@Test
	public void driverModify(){

		List<Actor> actors = new ArrayList<Actor>();
		actors.addAll(adminService.findAll());
		actors.addAll(customerService.findAll());
		
		Object testingData[][] = {
				//Usuario autenticado y existe receptor.Modifico el sender antes de enviar el mensaje
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"titulo","text",1,null},
				//Usuario autenticado y existe receptor.Modifico el recipient antes de enviar el mensaje
				{actors.get(0).getUserAccount().getUsername(),actors.get(1).getId(),"titulo","text",2,null},
			
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateModify((String)testingData[i][0], 
					(int) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(int) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}
	
	protected void template(String username,int recipient,String title, String text,Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			//Busco el actor que envía el mensaje
			Actor s = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (s == null){
				s = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
			if (this.customerService.findByUserAccountId(LoginService.getPrincipal().getId())==null &&
				this.adminService.findByUserAccountId(LoginService.getPrincipal().getId())==null	){
				s=null;
			}
				
			//Busco el actor que recibe el mensaje
			Actor r= customerService.findOne(recipient);
			if(r==null){
				r=adminService.findOne(recipient);
			}
			if(adminService.findOne(recipient)==null && customerService.findOne(recipient)==null ){
				r=null;
			}
			
			//Creo y envío el mensaje
			Message m = messageService.create(s, r);
			m.setText(text);
			m.setTitle(title);
			messageService.save(m);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	protected void templateModify(String username,int recipient,String title, String text,int sr,Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			
			//Busco el actor que envía el mensaje
			Actor s = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (s == null){
				s = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
				
			//Busco el actor que recibe el mensaje
			Actor r= customerService.findOne(recipient);
			if(r==null){
				r=adminService.findOne(recipient);
			}
			
			//Modifico actor que escribe o que recibe el mensaje
			if(sr==1){
				s.setName("New name");
			}else if(sr==2){
				r.setName("New name");
			}
			
			//Creo y envío el mensaje
			Message m = messageService.create(s, r);
			m.setText(text);
			m.setTitle(title);
			messageService.save(m);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	@Test
	public void driverCreate(){

		List<Actor> actors = new ArrayList<Actor>();
		actors.addAll(adminService.findAll());
		actors.addAll(customerService.findAll());
		
		Object testingData[][] = {
				//Creo el usuario que va a enviar el mensaje de nuevas.
				{"name",actors.get(1).getId(),"titulo","text",1,null},
				//Creo el usuario que va a recibir el mensaje de nuevas.
				{actors.get(0).getUserAccount().getUsername(),-30,"titulo","text",2,null},
				
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreate((String)testingData[i][0], 
					(int) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(int) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}
	
	protected void templateCreate(String username,int recipient,String title, String text,int sr,Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			Actor r=null;
			if(sr==1){
				final Collection<Authority> authorities = new ArrayList<Authority>();
				final Authority a = new Authority();
				a.setAuthority(Authority.CUSTOMER);
				authorities.add(a);
				final UserAccount ua = new UserAccount();
				ua.setUsername("username");
				ua.setPassword("password");
				ua.setAuthorities(authorities);
				
				final Customer c = this.customerService.create(ua);
				c.setName("name");
				c.setSurname("surname");
				c.setEmail("email@hola.com");
				c.setPhone("+34122332687");
				
				
				this.customerService.save(c);
				authenticate(c.getUserAccount().getUsername());
				
				//Busco el actor que recibe el mensaje
				r= customerService.findOne(recipient);
				if(r==null){
					r=adminService.findOne(recipient);
				}
			}else if(sr==2){
				final Collection<Authority> authorities = new ArrayList<Authority>();
				final Authority a = new Authority();
				a.setAuthority(Authority.ADMIN);
				authorities.add(a);
				final UserAccount ua = new UserAccount();
				ua.setUsername("username2");
				ua.setPassword("password");
				ua.setAuthorities(authorities);
				
				final Administrator c = this.adminService.create(ua);
				c.setName("name");
				c.setSurname("surname");
				c.setEmail("email@hola.com");
				c.setPhone("+34122332687");
				
				this.adminService.save(c);
				r=c;
				authenticate(username);
			}
			
			//Busco el actor que envía el mensaje
			Actor s = this.customerService.findByUserAccountId(LoginService.getPrincipal().getId());
			if (s == null){
				s = this.adminService.findByUserAccountId(LoginService.getPrincipal().getId());
			}
				
			//Creo y envío el mensaje
			Message m = messageService.create(s, r);
			m.setText(text);
			m.setTitle(title);
			messageService.save(m);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
}
