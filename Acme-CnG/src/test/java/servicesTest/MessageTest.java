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

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class MessageTest extends AbstractTest {

	@Autowired
	private MessageService messageService;
	@Autowired
	private CustomerService customerService;

	//-------------Create Message tests-------------
	@Test
	public void driverCreate(){
		Object testingData[][] = {
				{29, 30, "customer1",null},
				{0, 29, "customer1",IllegalArgumentException.class},
				{29, 0, "customer1",IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreate((int)testingData[i][0], (int)testingData[i][1],
				(String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}
	
	protected void templateCreate(int sender, int recipient, String username, Class<?> expected){
		Class<?> caught;
		Actor s = null;
		Actor r = null;
		caught=null;
		try{
			authenticate(username);
			if (sender!=0){
				s = customerService.findOne(sender);
				
			}
			if(recipient!=0){
				r = customerService.findOne(recipient);
			}
			
			messageService.create(s, r);
			
			unauthenticate();
		}catch(Throwable oops){
			caught =  oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	//-------------Save Message tests-------------
	@Test
	public void driverSave(){
		Object testingData[][] = {
				{Calendar.getInstance().getTime(),"title","text", "customer1",null},
				{null,"title","text","customer1",IllegalArgumentException.class},
				{Calendar.getInstance().getTime(),null,"text","customer1",IllegalArgumentException.class},
				{Calendar.getInstance().getTime(),"title",null,"customer1",IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateSave((Date)testingData[i][0], (String)testingData[i][1],
				(String)testingData[i][2], (String)testingData[i][3], 
				(Class<?>) testingData[i][4]);
		}
	}
	
	protected void templateSave(Date moment, String title, String text, String username, Class<?> expected){
		Class<?> caught;
		caught=null;
		try{
			authenticate(username);
			Actor s = customerService.findOne(29);
			Actor r = customerService.findOne(30);
			
			Message save = messageService.create(s, r);
			
			save.setMoment(moment);
			save.setTitle(title);
			save.setText(text);
			
			messageService.save(save);
			
			unauthenticate();
		}catch(Throwable oops){
			caught =  oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	@Test
	public void driverDelete(){
		Object testingData[][] = {
				{31, "customer1",null},
				{45, "customer1", IllegalArgumentException.class},
				{31, "customer2",IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateDelete((int) testingData[i][0],(String) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templateDelete(int messageId, String username, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);

			Message m = messageService.findOne(messageId);
			messageService.delete(m);
			
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
