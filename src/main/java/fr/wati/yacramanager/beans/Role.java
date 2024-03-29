/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Rachid Ouattara
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@SuppressWarnings("serial")
public class Role implements Serializable {

	public static final String ADMIN = "ROLE_ADMIN";
	public static final String SSII_ADMIN = "ROLE_SSII_ADMIN";
	public static final String SALARIE = "ROLE_SALARIE";
	public static final String INDEP = "ROLE_INDEP";
	public static final String ANONYMOUS = "ROLE_ANONYMOUS";

	public static List<String> availabeRoles = new ArrayList<String>();

	static {
		availabeRoles.add(SSII_ADMIN);
		availabeRoles.add(SALARIE);
		availabeRoles.add(INDEP);
		availabeRoles.add(ADMIN);
		availabeRoles.add(ANONYMOUS);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String role;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "userId") })
	private Set<Users> users = new HashSet<>();

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the users
	 */
	public Set<Users> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
