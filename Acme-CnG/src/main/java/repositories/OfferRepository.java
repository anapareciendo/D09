
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

	@Query("select o from Offer o where o.banned=false")
	Collection<Offer> findNoBannedRequest();

	@Query("select d from Offer d where d.title like '%'||:keyword||'%' or d.description like '%'||:keyword||'%' or d.destination.address like '%'||:keyword||'%' or d.origin.address like '%'||:keyword||'%'")
	Collection<Offer> searchOffers(@Param("keyword") String keyword);

}
