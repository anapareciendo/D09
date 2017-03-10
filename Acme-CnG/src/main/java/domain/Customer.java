package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Customer extends Actor{

	//---------------------Relationships--------------------------
	private Collection<Offer> offers;
	private Collection<Request> requests;
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "customer")
	public Collection<Offer> getOffers() {
		return offers;
	}
	
	public void setOffers(Collection<Offer> offers) {
		this.offers = offers;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "customer")
	public Collection<Request> getRequests() {
		return requests;
	}
	
	public void setRequests(Collection<Request> requests) {
		this.requests = requests;
	}
	
	
	
}
