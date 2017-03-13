
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Application extends DomainEntity {

	//----------------------Attributes-------------------------
	private Status	status;


	@Valid
	@NotNull
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(final Status status) {
		this.status = status;
	}


	//---------------------Relationships--------------------------
	private Customer	customer;
	private Demand		demand;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Customer getCustomer() {
		return this.customer;
	}
	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Demand getDemand() {
		return this.demand;
	}
	public void setDemand(final Demand demand) {
		this.demand = demand;
	}

}
