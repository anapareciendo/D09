
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.OfferRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Comment;
import domain.Offer;
import domain.Place;

@Service
@Transactional
public class OfferService {

	//Managed repository
	@Autowired
	private OfferRepository	offerRepository;


	//Constructors
	public OfferService() {
		super();
	}

	//Simple CRUD methods
	public Offer create(final Place origin, final Place destination) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Offer res;
		res = new Offer();

		res.setOrigin(origin);
		res.setDestination(destination);
		res.setMoment(Calendar.getInstance().getTime());
		res.setApplications(new ArrayList<Application>());
		res.setComments(new ArrayList<Comment>());

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

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");

		
		Assert.notNull(offer.getOrigin());
		Assert.notNull(offer.getDestination());

		Assert.isTrue(offer.getTitle() != "" && offer.getTitle() != null);
		Assert.isTrue(offer.getDescription() != "" && offer.getDescription() != null);
		Assert.isTrue(offer.getMoment() != null);

		Offer res = this.offerRepository.save(offer);
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

}
