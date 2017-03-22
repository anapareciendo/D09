package useCases;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.LoginService;
import services.CustomerService;
import services.DemandService;
import services.PlaceService;
import utilities.AbstractTest;
import domain.Customer;
import domain.Demand;
import domain.Place;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PostAnOffer extends AbstractTest{
	
	//Use case: Post an offer (Level C)
	@Autowired
	private DemandService demandService;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void driver(){
		Object testingData[][] = {
				{"title","description",Calendar.getInstance().getTime(),Boolean.FALSE,"customer1",null},
				{"title","description",Calendar.getInstance().getTime(),Boolean.FALSE,"admin1",IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(String) testingData[i][1],
					(Date) testingData[i][2],
					(Boolean) testingData[i][3],
					(String) testingData[i][4],
					(Class<?>) testingData[i][5]);
		}
	}
	
	protected void template(String title, String description,
		Date moment,boolean banned, String user, Class<?> expected){
		
		Class<?> caught;
		caught = null;
		try{
			authenticate(user);
			List<Place> places = new ArrayList<Place>();
			places.addAll(placeService.findAll());
			if(!places.isEmpty()){
				Collections.shuffle(places);
				Place o = places.get(0);
				Collections.shuffle(places);
				Place d = places.get(0);
				Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
				Demand save = demandService.createOffer(o, d, c);
				save.setTitle(title);
				save.setDescription(description);
				save.setMoment(moment);
				save.setBanned(banned);
				
				demandService.save(save);
			}
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
