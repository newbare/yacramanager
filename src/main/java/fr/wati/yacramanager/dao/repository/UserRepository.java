/**
 * 
 */
package fr.wati.yacramanager.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Users;

/**
 * @author Rachid Ouattara
 *
 */
public interface UserRepository extends JpaRepository<Users, Long>,JpaSpecificationExecutor<Users>{

//	Users findByUsername(String username);
	Users findByEmail(String email);
	Page<Users> findByEmailLike(String email, Pageable pageable);
	/**
	 * @param key
	 * @return
	 */
	Users getUserByActivationKey(String key);
}
