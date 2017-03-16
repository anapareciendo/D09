package useCases;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.PlaceService;
import services.RequestService;
import utilities.AbstractTest;
import domain.Place;
import domain.Request;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PostARequest extends AbstractTest{
	
	//Use case: Post a request (Level C)
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private PlaceService placeService;
	
	@Test
	public void driver(){
		Object testingData[][] = {
				{37,40,"title","description",Calendar.getInstance().getTime(),Boolean.FALSE,"customer1",null},
				{37,40,"title","description",Calendar.getInstance().getTime(),Boolean.FALSE,"noUser",IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((int)testingData[i][0], 
					(int) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(Date) testingData[i][4],
					(Boolean) testingData[i][5],
					(String) testingData[i][6],
					(Class<?>) testingData[i][7]);
		}
	}
	
	protected void template(int origin, int destination, String title, String description,
		Date moment,boolean banned, String user, Class<?> expected){
		
		Class<?> caught;
		caught = null;
		try{
			authenticate(user);
			
			Place o = placeService.findOne(origin);
			Place d = placeService.findOne(destination);
			Request save = requestService.create(o, d);
			save.setTitle(title);
			save.setDescription(description);
			save.setMoment(moment);
			save.setBanned(banned);
			
			requestService.save(save);
			
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
