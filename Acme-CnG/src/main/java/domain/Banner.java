
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Banner extends DomainEntity {

	//----------------------Attributes-------------------------
	private String	logo;


	@SafeHtml
	@NotNull
	public String getLogo() {
		return this.logo;
	}
	public void setLogo(final String logo) {
		this.logo = logo;
	}

}
