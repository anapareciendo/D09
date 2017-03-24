
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Comment;
import domain.Customer;
import domain.Demand;
import domain.Message;

@Service
@Transactional
public class AdministratorService {

	//Managed repository
	@Autowired
	private AdministratorRepository	administratorRepository;


	//Validator
	/*
	 * @Autowired
	 * private Validator validator;
	 */

	//Supporting services

	//Constructors
	public AdministratorService() {
		super();
	}

	//Simple CRUD methods
	public Administrator create(final UserAccount ua) {
		Administrator res;
		res = new Administrator();
		res.setPostComments(new ArrayList<Comment>());
		res.setComments(new ArrayList<Comment>());
		res.setReceivedMessages(new ArrayList<Message>());
		res.setSenderMessages(new ArrayList<Message>());
		res.setUserAccount(ua);
		return res;
	}

	public Collection<Administrator> findAll() {
		final Collection<Administrator> res = this.administratorRepository.findAll();
		return res;
	}

	public Administrator findOne(final int adminId) {
		final Administrator res = this.administratorRepository.findOne(adminId);
		return res;
	}

	public Administrator save(final Administrator admin) {
		Assert.notNull(admin, "The administrator to save cannot be null.");
		final Administrator res = this.administratorRepository.save(admin);
		administratorRepository.flush();
		return res;
	}

	public void delete(final Administrator admin) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(admin, "The adminstrator to delete cannot be null.");
		Assert.isTrue(this.administratorRepository.exists(admin.getId()));

		Assert.isNull(admin.getPostComments().isEmpty(), "The administrator cannot be delete with post comments");
		Assert.isNull(admin.getComments().isEmpty(), "The administrator cannot be delete with comments");
		Assert.isNull(admin.getReceivedMessages().isEmpty(), "The administrator cannot be delete with messages");
		Assert.isNull(admin.getSenderMessages().isEmpty(), "The administrator cannot be delete with sender messages");

		this.administratorRepository.delete(admin);
	}

	//Utilites methods
	public Administrator findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.administratorRepository.findByUserAccountId(id);
	}

	//Para el dashboard

	
	 private void isAdministrator(){
	  UserAccount ua = LoginService.getPrincipal();
	  Assert.notNull(ua);
	  Authority a = new Authority();
	  a.setAuthority(Authority.ADMIN);
	  Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a administrator for this action.");
	 }
	 
	 //Level C
	 
	 //Ratio of offers versus requests
	 public Demand ratioOffer(){
			this.isAdministrator();
			return administratorRepository.ratioOffer();
		}
	 public Demand ratioRequest(){
			this.isAdministrator();
			return administratorRepository.ratioRequest();
		}
	 
	//Average number of offers per customer.
	 public Double avgOffersCustomer(){
			this.isAdministrator();
			return administratorRepository.avgOffersCustomer();
		}
	 
	//Average number request per customer.
	 public Double avgRequestsCustomer(){
			this.isAdministrator();
			return administratorRepository.avgRequestsCustomer();
		}
	 
	//Average number of applications per offer.
	 public Double avgApplicationsOffer(){
			this.isAdministrator();
			return administratorRepository.avgApplicationsOffer();
		}
	 
	//Average number of applications per request.
	 public Double avgApplicationsRequest(){
			this.isAdministrator();
			return administratorRepository.avgApplicationsRequest();
		}
	 
	//The customer who has more applications accepted
	 public Customer customerMoreApplicationAccepted(){
			this.isAdministrator();
			return administratorRepository.customerMoreApplicationAccepted();
		}
	//The customer who has more applications denied.
	 public Customer customerMoreApplicationDenied(){
			this.isAdministrator();
			return administratorRepository.customerMoreApplicationDenied();
		}
	 
	 //Level B
	 
	//Average number of comments per actor
	 public Double avgCommentPerActor(){
			this.isAdministrator();
			return administratorRepository.avgCommentPerActor();
		}
	 
	//Average number of comments per offer
	 public Double avgCommentPerOffer(){
			this.isAdministrator();
			return administratorRepository.avgCommentPerOffer();
		}
	 
	//Average number of comments per request
	 public Double avgCommentPerRequest(){
			this.isAdministrator();
			return administratorRepository.avgCommentPerRequest();
		}
	 
	// Average number of comments posted by administrators
	 public Double avgCommentPostAdmin(){
			this.isAdministrator();
			return administratorRepository.avgCommentPostAdmin();
		}
	 
	// Average number of comments posted by customer
	 public Double avgCommentPostCustomer(){
			this.isAdministrator();
			return administratorRepository.avgCommentPostCustomer();
		}
	 
	 //Level A
	 
	//The minimum, the average, and the maximum number of messages sent per actor
	 public Double minSentMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.minSentMessagePerActor();
		}
	//The maximum number of messages sent per actor
	 public Double maxSentMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.maxSentMessagePerActor();
		}
	 
	//The average of messages sent per actor
	 public Double avgSentMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.avgSentMessagePerActor();
		}
	 
	//The minimum of messages received per actor.
	 public Double minReciveMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.minReciveMessagePerActor();
		}
	 
	 //The maximum number of messages received per actor.
	 public Double maxReciveMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.maxReciveMessagePerActor();
		}
	 
	//The average of messages received per actor.
	 public Double avgReciveMessagePerActor(){
			this.isAdministrator();
			return administratorRepository.avgReciveMessagePerActor();
		}
	 
	//The actors who have sent more messages
	 public Collection<Actor> actorsSentMoreMessages(){
			this.isAdministrator();
			return administratorRepository.actorsSentMoreMessages();
		}
	 
	//The actors who have got more messages
	 public Collection<Actor> actorsGotMoreMessages(){
			this.isAdministrator();
			return administratorRepository.actorsGotMoreMessages();
		}


	 

	 





	 
	 



	


}
