
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
	
	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByUserAccountId(int id);
	
	//Level C
	
	//Ratio of offers versus requests
	//Select o from Offer o;
	//Select r from Request r;
	
	//Average number of offers and request per customer.
	//select avg(c.offers.size) from Customer c;
	//Select avg(c.requests.size) from Customer c;
	
	//Average number of applications per offer or request.
	//Select avg(a.demand.size) from Application a;
	
	//The customer who has more applications accepted
	//select distinct(d.customer) from Demand d join d.application a where d.application.size = (select max(d.application.size) from Demand d join d.application a where a.status = 1) AND r.status = 1
	
	//The customer who has more applications denied.
	//Probar la de arriba
	
	//Level B
	
	//Average number of comments per actor
	//Select avg(a.comments.size) from Actor a;
	
	
	//Average number of comments per offer
	//Average number of comments per request
	//Probar la primera
	
	// Average number of comments posted by administrators and customer
	//Select avg(a.postComments) from Actor a;
	
	//The actors who have posted ±10% the average number of comments per actor
	
	//Level A
	
	
	
	
}
