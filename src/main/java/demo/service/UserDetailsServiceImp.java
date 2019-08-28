package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import demo.dao.Dao;
import demo.model.Client;
import demo.model.Credential;

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	private Dao dao;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) {
		Credential credential=null;
		try {
            credential = dao.findCredentialByName(username);
		} catch (CannotCreateTransactionException e) {
//			log
		}
		UserBuilder builder = null;
        Client client;
		if (credential != null) {
			client = credential.getClient();
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(credential.getPassword());
			builder.roles(credential.getRole());
		} else {
			String message = "User: " + username + " not found";
            
			throw new UsernameNotFoundException(message);
		}
		
		return builder.build();
	}
}
