package fr.wati.yacramanager.beans;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserPasswordEntityListener {

	private PasswordEncoder passwordEncoder;
	
	public UserPasswordEntityListener() {
		passwordEncoder=new BCryptPasswordEncoder(11);
	}
	@PrePersist
	public void create(Users users){
		encodePassword(users);
	}
	private void encodePassword(Users users) {
		//Encode password before save
		if(users!= null && users.getPassword()!=null){
			users.setPassword(passwordEncoder.encode(users.getPassword()));
		}
	}
	@PreUpdate
	public void update(Users users){
		//encodePassword(users);
	}
	
	@PostLoad
	public void load(Users users){
		//Preserve viewing password
		//users.setPassword(null);
	}
	
}