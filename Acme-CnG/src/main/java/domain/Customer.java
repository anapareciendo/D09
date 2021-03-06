
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
public class Customer extends Actor {

	//---------------------Relationships--------------------------
	private Collection<Application>	applications;
	private Collection<Demand> demands;

	@Valid
	@NotNull
	@OneToMany(mappedBy="customer")
	public Collection<Demand> getDemands() {
		return demands;
	}
	public void setDemands(Collection<Demand> demands) {
		this.demands = demands;
	}
	@Valid
	@NotNull
	@OneToMany(mappedBy = "customer")
	public Collection<Application> getApplications() {
		return this.applications;
	}
	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

}
