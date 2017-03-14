
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.banned=false")
	Collection<Request> findNoBannedRequest();

	@Query("select d from Request d where d.title like '%'||:keyword||'%' or d.description like '%'||:keyword||'%' or d.destination.address like '%'||:keyword||'%' or d.origin.address like '%'||:keyword||'%'")
	Collection<Request> searchRequests(@Param("keyword") String keyword);

}
