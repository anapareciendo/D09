
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Comment;
import domain.Place;
import domain.Request;

@Service
@Transactional
public class RequestService {

	//Managed repository
	@Autowired
	private RequestRepository	requestRepository;


	//Constructors
	public RequestService() {
		super();
	}

	//Simple CRUD methods
	public Request create(final Place origin, final Place destination) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Request res;
		res = new Request();

		res.setOrigin(origin);
		res.setDestination(destination);
		res.setMoment(Calendar.getInstance().getTime());
		res.setApplications(new ArrayList<Application>());
		res.setComments(new ArrayList<Comment>());
		res.setBanned(false);

		return res;
	}

	public Collection<Request> findAll() {
		final Collection<Request> res = this.requestRepository.findAll();
		return res;
	}

	public Request findOne(final int requestId) {
		final Request res = this.requestRepository.findOne(requestId);
		return res;
	}

	public Request save(final Request request) {
		Assert.notNull(request, "The offer to save cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be a Customer for this action");

		Assert.notNull(request.getOrigin());
		Assert.notNull(request.getDestination());

		Assert.isTrue(request.getTitle() != "" && request.getTitle() != null);
		Assert.isTrue(request.getDescription() != "" && request.getDescription() != null);
		Assert.isTrue(request.getMoment() != null);

		final Request res = this.requestRepository.save(request);
		res.setMoment(Calendar.getInstance().getTime());

		return res;
	}

	public void delete(final Request request) {

		Assert.notNull(request, "The request to delete cannot be null.");
		Assert.isTrue(this.requestRepository.exists(request.getId()));

		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(request.getCustomer().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the request");

		this.requestRepository.delete(request);
	}

	//Utility Methods
	public Collection<Request> findNoBannedRequest() {
		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);
		final Authority c = new Authority();
		c.setAuthority(Authority.CUSTOMER);
		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a) || ua.getAuthorities().contains(c), "You must to be autenticate for this action");

		return this.requestRepository.findNoBannedRequest();
	}
	public Collection<Request> searchRequest(final String keyword) {
		final Authority b = new Authority();
		b.setAuthority(Authority.CUSTOMER);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be a Customer for this action");

		return this.requestRepository.searchRequests(keyword);
	}
	
	public void ban(final int requestId) {
		final Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(b), "You must to be an Admin for this action");

		final Request request= this.requestRepository.findOne(requestId);
		Assert.notNull(request, "This id is not from an request");

		request.setBanned(true);
		this.requestRepository.save(request);
	}

}
