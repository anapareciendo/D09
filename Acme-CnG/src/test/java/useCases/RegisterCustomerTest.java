package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CustomerService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterCustomerTest extends AbstractTest{
	
	//Use case: view the banner (Level C)
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void driver(){
		Object testingData[][] = {
				{"customer1",null},
				{"",null},

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
				
				unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
