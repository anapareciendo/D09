package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.OfferService;
import services.RequestService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FindNotBannedDemads extends AbstractTest{

	//Use case: Find not banned demands (Level C)
	@Autowired
	private OfferService offerService;
	@Autowired
	private RequestService requestService;
	
	@Test
	public void driverCreate(){
		Object testingData[][] = {
				{"customer1",null},
				{"",IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
		}
	}
	
	protected void template(String user, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
			authenticate(user);
			offerService.findNoBannedOffers();
			requestService.findNoBannedRequest();
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

	
	
}
