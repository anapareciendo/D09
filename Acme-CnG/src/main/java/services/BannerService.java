
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BannerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Banner;

@Service
@Transactional
public class BannerService {

	//Managed repository
	@Autowired
	private BannerRepository	bannerRepository;


	//Validator
	/*
	 * @Autowired
	 * private Validator validator;
	 */

	//Supporting services

	//Constructors
	public BannerService() {
		super();
	}

	//Simple CRUD methods
	public Banner create() {
		Banner res;
		res = new Banner();
		return res;
	}

	public Collection<Banner> findAll() {
		final Collection<Banner> res = this.bannerRepository.findAll();
		return res;
	}

	public Banner findOne(final int bannerId) {
		final Banner res = this.bannerRepository.findOne(bannerId);
		return res;
	}

	public Banner save(final Banner banner) {
		Assert.notNull(banner, "The banner to save cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be an Admin for this action");

		final Banner res = this.bannerRepository.save(banner);

		return res;
	}

}
