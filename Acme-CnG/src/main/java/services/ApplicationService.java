
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
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	//Managed repository
	@Autowired
	private ApplicationRepository	applicationRepository;


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
		res.setStatus(Status.PENDING);
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
		Assert.notNull(application, "The application to delete cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");

		Assert.isTrue(application.getCustomer().getUserAccount().equals(ua) || application.getDemand().getCustomer().getUserAccount().equals(ua), "You are not the owner of this application");

		final Application res = this.applicationRepository.save(application);

		return res;
	}

	public void delete(final Application application) {
		Assert.notNull(application, "The application to delete cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");

		Assert.isTrue(application.getCustomer().getUserAccount().equals(ua), "You are not the owner of this application");

		this.applicationRepository.delete(application);

	}

	//Utilites methods
	public Collection<Application> findApplicationMyDemand() {
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this acction");

		return this.applicationRepository.findApplicationMyDemand(ua.getId());
	}

	public void accept(final int applicationId) {
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this acction");

		final Application application = this.applicationRepository.findOne(applicationId);
		Assert.notNull(application, "The id is not from an application");
		Assert.isTrue(application.getCustomer().getUserAccount().equals(ua), "You are not the owner of this application");

		application.setStatus(Status.ACCEPTED);
		this.applicationRepository.save(application);
	}

	public void deny(final int applicationId) {
		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this acction");

		final Application application = this.applicationRepository.findOne(applicationId);
		Assert.notNull(application, "The id is not from an application");
		Assert.isTrue(application.getCustomer().getUserAccount().equals(ua), "You are not the owner of this application");

		application.setStatus(Status.DENIED);
		this.applicationRepository.save(application);
	}
}
