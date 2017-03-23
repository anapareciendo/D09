package useCases;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;
import forms.ActorForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterCustomerTest extends AbstractTest{
	
	/* *---- RegisterCustomer.-----*
	  -El orden de los parametros es: username, password, name, surname, email, phone, error esperado
	  
	  Cobertura del test:
			//El usuario se registra correctamente(test positivo)
			//El usuario con un name nulo(test negativo)
	 */
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void driverCreate(){
		Object testingData[][] = {
				{"customerTest","pass","Aloy","Ramos","aloyR@gmail.com","+34122332687",null},
				{"customerTest2","pass",null,"Ramos","aloyR@gmail.com","+34122332687",IllegalArgumentException.class}

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
				ActorForm actor = new ActorForm();
				actor.setUsername(username);
				actor.setPassword1(password);
				actor.setPassword2(password);
				actor.setName(name);
				actor.setSurname(surname);
				actor.setEmail(email);
				actor.setPhone(phone);
				String[] conditions={"acepto"};
				actor.setConditions(conditions);
				

				Customer res =customerService.reconstruct(actor, null); 
				customerService.save(res);
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
