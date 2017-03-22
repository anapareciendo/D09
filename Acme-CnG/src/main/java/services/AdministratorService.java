
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Comment;
import domain.Message;

@Service
@Transactional
public class AdministratorService {

	//Managed repository
	@Autowired
	private AdministratorRepository	administratorRepository;


	//Validator
	/*
	 * @Autowired
	 * private Validator validator;
	 */

	//Supporting services

	//Constructors
	public AdministratorService() {
		super();
	}

	//Simple CRUD methods
	public Administrator create(final UserAccount ua) {
		Administrator res;
		res = new Administrator();
		res.setPostComments(new ArrayList<Comment>());
		res.setComments(new ArrayList<Comment>());
		res.setReceivedMessages(new ArrayList<Message>());
		res.setSenderMessages(new ArrayList<Message>());
		res.setUserAccount(ua);
		administratorRepository.flush();
		return res;
	}

	public Collection<Administrator> findAll() {
		final Collection<Administrator> res = this.administratorRepository.findAll();
		return res;
	}

	public Administrator findOne(final int adminId) {
		final Administrator res = this.administratorRepository.findOne(adminId);
		return res;
	}

	public Administrator save(final Administrator admin) {
		Assert.notNull(admin, "The administrator to save cannot be null.");
		final Administrator res = this.administratorRepository.save(admin);
		administratorRepository.flush();

		return res;
	}

	public void delete(final Administrator admin) {
		final UserAccount ua = LoginService.getPrincipal();
		Assert.notNull(ua);
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a admin to delete an actor.");

		Assert.notNull(admin, "The adminstrator to delete cannot be null.");
		Assert.isTrue(this.administratorRepository.exists(admin.getId()));

		Assert.isNull(admin.getPostComments().isEmpty(), "The administrator cannot be delete with post comments");
		Assert.isNull(admin.getComments().isEmpty(), "The administrator cannot be delete with comments");
		Assert.isNull(admin.getReceivedMessages().isEmpty(), "The administrator cannot be delete with messages");
		Assert.isNull(admin.getSenderMessages().isEmpty(), "The administrator cannot be delete with sender messages");

		this.administratorRepository.delete(admin);
	}

	//Utilites methods
	public Administrator findByUserAccountId(final int id) {
		Assert.notNull(id);
		return this.administratorRepository.findByUserAccountId(id);
	}

	//Para el dashboard

	/*
	 * private void isAdministrator(){
	 * UserAccount ua = LoginService.getPrincipal();
	 * Assert.notNull(ua);
	 * Authority a = new Authority();
	 * a.setAuthority(Authority.ADMIN);
	 * Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a administrator for this action.");
	 * }
	 */

}
