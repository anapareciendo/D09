
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Demand;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Integer> {

	@Query("select d from Demand d where d.banned=false and d.type=1") //REQUEST
	Collection<Demand> findNoBannedRequests();

	@Query("select d from Demand d where d.banned=false and d.type=0") //OFFER
	Collection<Demand> findNoBannedOffers();
	
//	@Query("select d from Demand d where d.customer.userAccount.id=?1 and d.type=1") //REQUEST
//	Collection<Demand> findMyRequests(int uaId);
//
//	@Query("select d from Demand d where d.customer.userAccount.id=?1 and d.type=0") //OFFER
//	Collection<Demand> findMyOffers(int uaId);
	
	@Query("select d from Demand d where (d.title like '%'||:keyword||'%' or d.description like '%'||:keyword||'%' or d.destination.address like '%'||:keyword||'%' or d.origin.address like '%'||:keyword||'%') and d.type=1 and d.banned=false")
	Collection<Demand> searchRequests(@Param("keyword") String keyword);
	
	@Query("select d from Demand d where (d.title like '%'||:keyword||'%' or d.description like '%'||:keyword||'%' or d.destination.address like '%'||:keyword||'%' or d.origin.address like '%'||:keyword||'%') and d.type=0 and d.banned=false")
	Collection<Demand> searchOffers(@Param("keyword") String keyword);

}
