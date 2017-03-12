
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Comment;
import domain.Customer;
import domain.Message;

@Service
@Transactional
public class CustomerService {

	//Managed repository
	@Autowired
	private CustomerRepository customerRepository;
	
	/*Validator
		@Autowired
		private Validator validator;*/



	//Constructors
	public CustomerService() {
		super();
	}

	//Simple CRUD methods
	public Customer create(UserAccount ua) {
		Customer res;
		res = new Customer();
		res.setPostComments(new ArrayList<Comment>());
		res.setComments(new ArrayList<Comment>());
		res.setReceivedMessages(new ArrayList<Message>());
		res.setSenderMessages(new ArrayList<Message>());
		res.setUserAccount(ua);
		return res;
	}

	public Collection<Customer> findAll() {
		Collection<Customer> res = customerRepository.findAll();
		return res;
	}

	public Customer findOne(int customerId) {
		Customer res = customerRepository.findOne(customerId);
		return res;
	}

	public Customer save(Customer customer) {
		Assert.notNull(customer, "The customer to save cannot be null.");
		Customer res = customerRepository.save(customer);

		return res;
	}

	public void delete(Customer customer) {
		UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(customer, "The customer to delete cannot be null.");
		Assert.isTrue(customerRepository.exists(customer.getId()));
		
		Assert.isNull(customer.getPostComments().isEmpty(), "The customer cannot be delete with post comments");
		Assert.isNull(customer.getComments().isEmpty(), "The customer cannot be delete with comments");
		Assert.isNull(customer.getReceivedMessages().isEmpty(), "The custome cannot be delete with messages");
		Assert.isNull(customer.getSenderMessages().isEmpty(), "The customer cannot be delete with sender messages");

		customerRepository.delete(customer);
	}

	//Utilites methods
	public Customer findByUserAccountId(int id){
		Assert.notNull(id);
		return customerRepository.findByUserAccountId(id);
	}
	
	
}
