package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Actor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DisplayActorTest extends AbstractTest{

	@Autowired
	private AdministratorService adminService;
	@Autowired
	private CustomerService customerService;
	
	
	@Test
	public void driver(){
		Object testingData[][] = {
				//Está autenticado
				{28,"customer1",null},
				//No está autenticado
				{28,"noUser",IllegalArgumentException.class},

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((int)testingData[i][0], 
					(String) testingData[i][1],
					(Class<?>) testingData[i][2]);
		}
	}
	
	protected void template(int uaId, String user, Class<?> expected){
		
		Class<?> caught;
		caught = null;
		try{
			authenticate(user);
			
			Actor a = customerService.findByUserAccountId(uaId);
			if (a == null){
				a = adminService.findByUserAccountId(uaId);
			}
			//Falta el not banned commentss
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
