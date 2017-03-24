
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select a from Application a where a.demand.customer.userAccount.id=?1 and a.demand.type=0")
	Collection<Application> findApplicationMyOffers(int id);
	
	@Query("select a from Application a where a.demand.customer.userAccount.id=?1 and a.demand.type=1")
	Collection<Application> findApplicationMyRequests(int id);

}
