package useCases;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.UserAccount;
import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;

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
	public void driverCreate(){
		Object testingData[][] = {
				{"customerTest","pass","Aloy","Ramos","aloyR@gmail.com","+34122332687",null},
				{"customerTest","pass",null,"Ramos","aloyR@gmail.com","+34122332687",IllegalArgumentException.class}

		};
		
		for(int i = 0; i < testingData.length; i++){
			template((String) testingData[i][0],
					(String) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(String) testingData[i][4],
					(String) testingData[i][5],
					(Class<?>) testingData[i][6]);
		}
	}
	
	protected void template(String username, String password, String name, String surname, String email, String phone, Class<?> expected){
		Class<?> caught;
		caught = null;
		try{
				Collection<Authority> authorities = new ArrayList<Authority>();
				Authority a = new Authority();
				a.setAuthority(Authority.CUSTOMER);
				authorities.add(a);
				UserAccount ua = new UserAccount();
				ua.setUsername(username);
				ua.setPassword(password);
				ua.setAuthorities(authorities);
				
				Customer save = customerService.create(ua);
				save.setName(name);
				save.setSurname(surname);
				save.setEmail(email);
				save.setPhone(phone);
				
				customerService.save(save);
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
