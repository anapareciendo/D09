
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlaceRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Place;

@Service
@Transactional
public class PlaceService {

	//Managed repository
	@Autowired
	private PlaceRepository	placeRepository;


	//Supporting services

	/*
	 * @Autowired
	 * private Validator validator;
	 */

	//Constructors
	public PlaceService() {
		super();
	}

	//Simple CRUD methods
	public Place create() {
		Place res;
		res = new Place();
		return res;
	}

	public Collection<Place> findAll() {
		final Collection<Place> res = this.placeRepository.findAll();
		return res;
	}

	public Place findOne(final int placeId) {
		final Place res = this.placeRepository.findOne(placeId);
		return res;
	}

	public Place save(final Place place) {
		Assert.notNull(place, "The place to save cannot be null.");
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a customer for this acction");
		Assert.notNull(place.getAddress(), "El lugar no puede ser nulo");
		final Place res = this.placeRepository.save(place);
		return res;
	}

}
