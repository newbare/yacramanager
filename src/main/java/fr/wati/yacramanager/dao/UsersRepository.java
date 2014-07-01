/**
 * 
 */
package fr.wati.yacramanager.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Users;

/**
 * @author Rachid Ouattara
 *
 */
public interface UsersRepository extends JpaRepository<Users, Long>{

	Users findByUsername(String username);
	Page<Users> findByUsernameLike(String username, Pageable pageable);
}
