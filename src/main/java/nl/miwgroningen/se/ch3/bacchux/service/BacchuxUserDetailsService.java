package nl.miwgroningen.se.ch3.bacchux.service;

import nl.miwgroningen.se.ch3.bacchux.repository.BacchuxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BacchuxUserDetailsService implements UserDetailsService {

    @Autowired
    BacchuxUserRepository bacchuxUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return bacchuxUserRepository.findByUsername(s).orElseThrow(
                () -> new UsernameNotFoundException("User " + s + " was not found")
        );
    }
}
