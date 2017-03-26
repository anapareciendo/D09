package useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.CommentService;
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
	@Autowired
	private CommentService commentService;
	
	private List<Actor> actors;
	
	@Before
    public void setup() {
		this.actors = new ArrayList<Actor>();
		this.actors.addAll(adminService.findAll());
		this.actors.addAll(customerService.findAll());
		
		Collections.shuffle(this.actors);
	}
	
	@Test
	public void driver(){
		Object testingData[][] = {
				//Está autenticado
				{actors.get(0).getUserAccount().getUsername(),null},
				//No está autenticado
				{null,IllegalArgumentException.class},

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
			int uaId = this.actors.get(0).getUserAccount().getId();
			Actor a = this.customerService.findByUserAccountId(uaId);
			if (a == null){
				a = this.adminService.findByUserAccountId(uaId);
			}
			this.commentService.findRealComments(a.getId());
			
			unauthenticate();
		
		} catch(Throwable oops){
			caught = oops.getClass();
		}
		checkExceptions(expected, caught);
	}

}
