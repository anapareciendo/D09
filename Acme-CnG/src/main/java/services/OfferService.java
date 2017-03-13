package services;

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
import domain.Offer;
import domain.Place;

@Service
@Transactional
public class OfferService {

	//Managed repository
	@Autowired
	private OfferRepository offerRepository;
	
	//Constructors
	public OfferService() {
		super();
	}
	
	//Simple CRUD methods
	public Offer create(Place origin, Place destination) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Offer res;
		res = new Offer();
		
		res.setOrigin(origin);
		res.setDestination(destination);
		res.setMoment(Calendar.getInstance().getTime());
		
		
		return res;
	}
	
	public Collection<Offer> findAll() {
		Collection<Offer> res = offerRepository.findAll();
		return res;
	}

	public Offer findOne(int offerId) {
		Offer res = offerRepository.findOne(offerId);
		return res;
	}
	
	public Offer save(Offer offer) {
		Assert.notNull(offer, "The offer to save cannot be null.");
		Offer res = offerRepository.save(offer);
		res.setMoment(Calendar.getInstance().getTime());
		Assert.notNull(offer.getOrigin());
		Assert.notNull(offer.getDestination());
		
		Assert.isTrue(offer.getTitle() != "" && offer.getTitle()!=null);
		Assert.isTrue(offer.getDescription() != "" && offer.getDescription()!=null);
		Assert.isTrue( offer.getMoment()!=null);
		
		return res;
	}
	
	public void delete(Offer offer) {
		
		Assert.notNull(offer, "The offer to delete cannot be null.");
		Assert.isTrue(offerRepository.exists(offer.getId()));
		
		Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		UserAccount ua=LoginService.getPrincipal();
		Assert.isTrue(offer.getCustomer().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the offer");
	
		offerRepository.delete(offer);
	}
	
	
	

}

