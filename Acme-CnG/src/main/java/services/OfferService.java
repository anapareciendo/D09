
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

import repositories.OfferRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Comment;
import domain.Customer;
import domain.Offer;
import domain.Place;

@Service
@Transactional
public class OfferService {

	//Managed repository
	@Autowired
	private OfferRepository	offerRepository;
	
	//Supporting services
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private Validator validator;



	//Constructors
	public OfferService() {
		super();
	}

	//Simple CRUD methods
	public Offer create(final Place origin, final Place destination, final Customer customer) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Offer res;
		res = new Offer();

		res.setOrigin(origin);
		res.setDestination(destination);
		res.setCustomer(customer);
		res.setMoment(Calendar.getInstance().getTime());
		res.setApplications(new ArrayList<Application>());
		res.setComments(new ArrayList<Comment>());
		res.setBanned(false);

		return res;
	}

	public Collection<Offer> findAll() {
		final Collection<Offer> res = this.offerRepository.findAll();
		return res;
	}

	public Offer findOne(final int offerId) {
		final Offer res = this.offerRepository.findOne(offerId);
		return res;
	}

	public Offer save(final Offer offer) {
		Assert.notNull(offer, "The offer to save cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(b), "You must to be a Customer or an Admin for this action");

		Assert.notNull(offer.getOrigin());
		Assert.notNull(offer.getDestination());

		Assert.isTrue(offer.getTitle() != "" && offer.getTitle() != null);
		Assert.isTrue(offer.getDescription() != "" && offer.getDescription() != null);
		Assert.isTrue(offer.getMoment() != null);

		final Offer res = this.offerRepository.save(offer);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Offer offer) {

		Assert.notNull(offer, "The offer to delete cannot be null.");
		Assert.isTrue(this.offerRepository.exists(offer.getId()));

		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(offer.getCustomer().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the offer");

		this.offerRepository.delete(offer);
	}

	//Utility Methods
	public Collection<Offer> findNoBannedOffers() {
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		final Authority c = new Authority();
		c.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(c), "You must to be autenticate for this action");

		return this.offerRepository.findNoBannedOffers();
	}

	public Collection<Offer> searchOffers(final String keyword) {
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a Customer for this action");

		return this.offerRepository.searchOffers(keyword);
	}

	public void ban(final int offerId) {
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be an Admin for this action");

		final Offer offer = this.offerRepository.findOne(offerId);
		Assert.notNull(offer, "This id is not from an offer");

		offer.setBanned(true);
		this.offerRepository.save(offer);
	}
	
	public Offer reconstruct(Offer offer, BindingResult binding) {
		Offer res;
		Customer c = customerService.findByUserAccountId(LoginService.getPrincipal().getId());
		if(offer.getId()==0){

			res = this.create(offer.getOrigin(), offer.getDestination(), c);
		}else{
			Offer aux = offerRepository.findOne(offer.getId());
			offerRepository.delete(aux);
			res= this.create(aux.getDestination(), aux.getOrigin(), c);
		}
		res.setTitle(offer.getTitle());
		res.setDescription(offer.getDescription());
		res.setBanned(offer.getBanned());
		
		validator.validate(res, binding);
		return res;
	}
}
