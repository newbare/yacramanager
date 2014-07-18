/**
 * 
 */
package fr.wati.yacramanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Role;

/**
 * @author Rachid Ouattara
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByRole(String role);
}
