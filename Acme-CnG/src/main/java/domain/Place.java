package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Place extends DomainEntity{

	//----------------------Attributes-------------------------
	private String address;
	private Integer latitude;
	private Integer longitude;
	
	@NotBlank
	@SafeHtml
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}
	
	public Integer getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}
	
	
}
