/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Actor;
import domain.Comment;
import domain.Commentable;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentTest extends AbstractTest {

	@Autowired
	private CommentService commentService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DemandService offerService;
	


	@Test
	public void driverCreate(){
		Object testingData[][] = {
				{33,41,null},
				{0,41, IllegalArgumentException.class},
				{33, 0, IllegalArgumentException.class}
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateCreate((int) testingData[i][0],
					(int) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void templateCreate(int sender, int commentable, Class<?> expected){
		Class<?> caught;
		Actor s = null;
		Commentable b = null;
		caught = null;
		try{
			authenticate("customer1");
			if (sender!=0){
				s = customerService.findOne(sender);
			}
			if(commentable!=0){
				b = offerService.findOne(commentable);
				
			}
			commentService.create(s, b);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
	@Test
	public void driverSave(){
		Object testingData[][] = {
				{"Comentario1",Calendar.getInstance().getTime(),"Blablabla",1,Boolean.FALSE,null},
				{"Comentario1",Calendar.getInstance().getTime(),"Blablabla",6,Boolean.FALSE,IllegalArgumentException.class},
				{null,Calendar.getInstance().getTime(),"Blablabla",1,Boolean.FALSE,IllegalArgumentException.class},
				{"Comentario1",Calendar.getInstance().getTime(),null,1,Boolean.FALSE,IllegalArgumentException.class},
				{"Comentario1",null,"Blablabla",1,Boolean.FALSE,IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			templateSave((String) testingData[i][0],
					(Date) testingData[i][1],
					(String) testingData[i][2],
					(int) testingData[i][3],
					(boolean) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}
	
	protected void templateSave(String title, Date moment, String text,int stars, boolean banned, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate("customer1");
			
			Comment save = commentService.findOne(49);
			save.setTitle(title);
			save.setMoment(moment);
			save.setText(text);
			save.setStars(stars);
			save.setBanned(banned);
			
			commentService.save(save);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	@Test
	public void driverDelete(){
		Object testingData[][] = {
				{"customer2",null},
				{"customer1",IllegalArgumentException.class},
		};
		
		for(int i = 0; i < testingData.length; i++){
			templateDelete((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void templateDelete(String username, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(username);
			Comment c = commentService.findOne(50);
			commentService.delete(c);
			unauthenticate();
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}
	
	
}
