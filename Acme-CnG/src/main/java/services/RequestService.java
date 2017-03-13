package services;

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
import domain.Place;
import domain.Request;

@Service
@Transactional
public class RequestService {

	//Managed repository
	@Autowired
	private RequestRepository requestRepository;
	
	//Constructors
	public RequestService() {
		super();
	}
	
	//Simple CRUD methods
	public Request create(Place origin, Place destination) {
		Assert.notNull(origin, "The origin place cannot be null");
		Assert.notNull(destination, "The destination place cannot be null");
		Request res;
		res = new Request();
		
		res.setOrigin(origin);
		res.setDestination(destination);
		res.setMoment(Calendar.getInstance().getTime());
		
		
		return res;
	}
	
	public Collection<Request> findAll() {
		Collection<Request> res = requestRepository.findAll();
		return res;
	}

	public Request findOne(int requestId) {
		Request res = requestRepository.findOne(requestId);
		return res;
	}
	
	public Request save(Request request) {
		Assert.notNull(request, "The request to save cannot be null.");
		Request res = requestRepository.save(request);
		res.setMoment(Calendar.getInstance().getTime());
		
		return res;
	}
	
	public void delete(Request request) {
		
		Assert.notNull(request, "The request to delete cannot be null.");
		Assert.isTrue(requestRepository.exists(request.getId()));
		
		Authority b = new Authority();
		b.setAuthority(Authority.ADMIN);

		UserAccount ua=LoginService.getPrincipal();
		Assert.isTrue(request.getCustomer().getUserAccount().equals(ua) || ua.getAuthorities().contains(b), "You are not the owner of the request");
	
		requestRepository.delete(request);
	}
	
	
	

}

