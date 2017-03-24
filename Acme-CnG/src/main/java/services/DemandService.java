
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DemandRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Comment;
import domain.Customer;
import domain.Demand;
import domain.Place;
import domain.Type;

@Service
@Transactional
public class DemandService {

	//Managed repository
	@Autowired
	private DemandRepository	demandRepository;

	//Supporting Services
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private Validator validator;

	//Constructors
	public DemandService() {
		super();
	}

	//Simple CRUD methods
	public Demand createOffer(final Place origin, final Place destination, final Customer customer) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Assert.notNull(customer, "The customer cannot be null");
		Demand res = new Demand();

		res.setOrigin(origin);
		res.setDestination(destination);
		res.setCustomer(customer);
		res.setMoment(Calendar.getInstance().getTime());
		res.setApplications(new ArrayList<Application>());
		res.setComments(new ArrayList<Comment>());
		res.setBanned(false);
		res.setType(Type.OFFER);

		return res;
	}
	
	public Demand createRequest(final Place origin, final Place destination, final Customer customer) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Assert.notNull(customer, "The customer cannot be null");
		Demand res = new Demand();

		res.setOrigin(origin);
		res.setDestination(destination);
		res.setCustomer(customer);
		res.setMoment(Calendar.getInstance().getTime());
		res.setApplications(new ArrayList<Application>());
		res.setComments(new ArrayList<Comment>());
		res.setBanned(false);
		res.setType(Type.REQUEST);

		return res;
	}

	public Collection<Demand> findAll() {
		final Collection<Demand> res = this.demandRepository.findAll();
		return res;
	}

	public Demand findOne(final int requestId) {
		final Demand res = this.demandRepository.findOne(requestId);
		return res;
	}

	public Demand save(final Demand demand) {
		Assert.notNull(demand, "The demand to save cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");

		Assert.notNull(demand.getOrigin());
		Assert.notNull(demand.getDestination());

		Assert.isTrue(demand.getTitle() != "" && demand.getTitle() != null);
		Assert.isTrue(demand.getDescription() != "" && demand.getDescription() != null);
		Assert.isTrue(demand.getMoment() != null);

		final Demand res = this.demandRepository.save(demand);

		return res;
	}

	public void delete(final Demand demand) {

		Assert.notNull(demand, "The request to delete cannot be null.");
		Assert.isTrue(this.demandRepository.exists(demand.getId()));

		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(demand.getCustomer().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the request");

		this.demandRepository.delete(demand);
	}

	//Utility Methods
	public Collection<Demand> findNoBannedRequests(){
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		final Authority c = new Authority();
		c.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(c), "You must to be autenticate for this action");

		return this.demandRepository.findNoBannedRequests();
	}
	public Collection<Demand> findNoBannedOffers(){
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		final Authority c = new Authority();
		c.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(c), "You must to be autenticate for this action");

		return this.demandRepository.findNoBannedOffers();
	}
	
//	public Collection<Demand> findMyRequests(){
//		final Authority c = new Authority();
//		c.setAuthority(Authority.CUSTOMER);
//		final UserAccount ua = LoginService.getPrincipal();
//		Assert.isTrue(ua.getAuthorities().contains(c), "You must to be a customer for this action");
//		return demandRepository.findMyRequests(ua.getId());
//	}
//
//	public Collection<Demand> findMyOffers(){
//		final Authority c = new Authority();
//		c.setAuthority(Authority.CUSTOMER);
//		final UserAccount ua = LoginService.getPrincipal();
//		Assert.isTrue(ua.getAuthorities().contains(c), "You must to be a customer for this action");
//		return demandRepository.findMyOffers(ua.getId());
//	}
	
	public Collection<Demand> searchRequest(final String keyword) {
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a Customer for this action");

		return this.demandRepository.searchRequests(keyword);
	}
	public Collection<Demand> searchOffers(final String keyword) {
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a Customer for this action");

		return this.demandRepository.searchOffers(keyword);
	}

	public void ban(final int requestId) {
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be an Admin for this action");

		final Demand request = this.demandRepository.findOne(requestId);
		Assert.notNull(request, "This id is not from an request");

		request.setBanned(true);
		this.demandRepository.save(request);
	}
	
	public Type apply(final int demandId) {
		Type res = Type.OFFER;
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a Customer for this action");

		final Demand demand = this.demandRepository.findOne(demandId);
		Assert.notNull(demand, "This id is not from an request");

		Application app = applicationService.create(customerService.findByUserAccountId(ua.getId()), demand);
		applicationService.save(app);
		
		if(demand.getType()==Type.REQUEST){
			res=Type.REQUEST;
		}
		return res;
	}
	
	public Demand reconstructOffer(Demand demand, BindingResult binding) {
		Demand res;
		Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		if(demand.getId()==0){
			res = this.createOffer(demand.getOrigin(), demand.getDestination(), c);
		}else{
			Demand aux = demandRepository.findOne(demand.getId());
			demandRepository.delete(aux);
			res= this.createOffer(aux.getDestination(), aux.getOrigin(), c);
		}
		res.setTitle(demand.getTitle());
		res.setDescription(demand.getDescription());
		res.setBanned(demand.getBanned());
		
		validator.validate(res, binding);
		return res;
	}
	
	public Demand reconstructRequest(Demand demand, BindingResult binding) {
		Demand res;
		Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		if(demand.getId()==0){
			res = this.createRequest(demand.getOrigin(), demand.getDestination(), c);
		}else{
			Demand aux = demandRepository.findOne(demand.getId());
			demandRepository.delete(aux);
			res= this.createRequest(aux.getDestination(), aux.getOrigin(), c);
		}
		res.setTitle(demand.getTitle());
		res.setDescription(demand.getDescription());
		res.setBanned(demand.getBanned());
		
		validator.validate(res, binding);
		return res;
	}

}
