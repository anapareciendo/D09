
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
	private BannerRepository bannerRepository;

	//Validator
	/*@Autowired
	private Validator validator;*/


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
		Collection<Banner> res = bannerRepository.findAll();
		return res;
	}

	public Banner findOne(int bannerId) {
		Banner res = bannerRepository.findOne(bannerId);
		return res;
	}

	public Banner save(Banner banner) {
		Assert.notNull(banner, "The banner to save cannot be null.");
		Banner res = bannerRepository.save(banner);

		return res;
	}

	public void delete(Banner banner) {
		UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete a banner.");

		Assert.notNull(banner, "The banner to delete cannot be null.");
		Assert.isTrue(bannerRepository.exists(banner.getId()));

		
		bannerRepository.delete(banner);
	}

	
}
