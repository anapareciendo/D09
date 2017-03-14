
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Customer;
import domain.Demand;

@Service
@Transactional
public class ApplicationService {

	//Managed repository
	@Autowired
	private ApplicationRepository	applicationRepository;


	/*
	 * Validator
	 * 
	 * @Autowired
	 * private Validator validator;
	 */

	//Constructors
	public ApplicationService() {
		super();
	}

	//Simple CRUD methods
	public Application create(final Customer customer, final Demand demand) {
		Assert.notNull(customer, "The customer cannot be null");
		Assert.notNull(demand, "The demand cannot be null");

		Application res;
		res = new Application();
		res.setCustomer(customer);
		res.setDemand(demand);
		return res;
	}

	public Collection<Application> findAll() {
		final Collection<Application> res = this.applicationRepository.findAll();
		return res;
	}

	public Application findOne(final int applicationId) {
		final Application res = this.applicationRepository.findOne(applicationId);
		return res;
	}

	public Application save(final Application application) {
		Assert.notNull(application, "The customer to save cannot be null.");
		
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");
		
		final Application res = this.applicationRepository.save(application);

		return res;
	}

	//Utilites methods

}
