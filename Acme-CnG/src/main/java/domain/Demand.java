
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "type, banned, title, description")})
public class Demand extends Commentable {

	//----------------------Attributes-------------------------

	private String	title;
	private String	description;
	private Date	moment;
	private boolean	banned;
	private Type type;

	@Valid
	@NotNull
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}
	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	public boolean getBanned() {
		return this.banned;
	}
	public void setBanned(final boolean banned) {
		this.banned = banned;
	}


	//---------------------Relationships--------------------------
	private Customer				customer;
	private Collection<Application>	applications;
	private Place					origin;
	private Place					destination;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Customer getCustomer() {
		return this.customer;
	}
	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "demand")
	public Collection<Application> getApplications() {
		return this.applications;
	}
	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Place getOrigin() {
		return this.origin;
	}
	public void setOrigin(final Place origin) {
		this.origin = origin;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Place getDestination() {
		return this.destination;
	}
	public void setDestination(final Place destination) {
		this.destination = destination;
	}

}
