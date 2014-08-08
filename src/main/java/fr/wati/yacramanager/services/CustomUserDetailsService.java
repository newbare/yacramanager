/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.repository.UsersRepository;

/**
 * @author Rachid Ouattara
 *
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsersRepository usersRepository;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public CustomUserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		Users user = usersRepository.findByUsername(username);
		if(user==null){
			throw new UsernameNotFoundException("The given username does not exist.");
		}
		return new CustomUserDetails(user);
	}

	@SuppressWarnings("serial")
	public static class CustomUserDetails extends User{

		private Users domainUser;
		
		public CustomUserDetails(Users domainUser){
			this(domainUser.getUsername(), domainUser.getPassword(), domainUser.isEnabled(),true,true,true,getGrantedAuthorities(domainUser.getRoles()));
			this.domainUser=domainUser;
		}
		
		/**
		 * @param username
		 * @param password
		 * @param enabled
		 * @param accountNonExpired
		 * @param credentialsNonExpired
		 * @param accountNonLocked
		 * @param authorities
		 */
		public CustomUserDetails(String username, String password,
				boolean enabled, boolean accountNonExpired,
				boolean credentialsNonExpired, boolean accountNonLocked,
				Collection<? extends GrantedAuthority> authorities) {
			super(username, password, enabled, accountNonExpired, credentialsNonExpired,
					accountNonLocked, authorities);
		}

		/**
		 * @param username
		 * @param password
		 * @param authorities
		 */
		public CustomUserDetails(String username, String password,
				Collection<? extends GrantedAuthority> authorities) {
			super(username, password, authorities);
		}

		/**
		 * @return the domainUser
		 */
		public Users getDomainUser() {
			return domainUser;
		}

		/**
		 * @param domainUser the domainUser to set
		 */
		public void setDomainUser(Users domainUser) {
			this.domainUser = domainUser;
		}
		
		public static List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (Role role : roles) {
				authorities.add(new SimpleGrantedAuthority(role.getRole()));
			}
			return authorities;
		}
	}
}
