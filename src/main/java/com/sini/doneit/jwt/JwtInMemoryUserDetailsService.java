package com.sini.doneit.jwt;

import com.sini.doneit.model.User;
import com.sini.doneit.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JwtInMemoryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserJpaRepository userJpaRepository;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByUsername(username);
        if(user != null){
            JwtUserDetails jwtUserDetails = new JwtUserDetails(user.getId(), user.getUsername(), user.getPassword(),
                    "ROLE_USER_2");
            return jwtUserDetails;
        }
        else{
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
        }
    }
}