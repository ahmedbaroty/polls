package com.dhb.learning.polls.auth.server.security;

import com.dhb.learning.polls.auth.server.repository.UserRepository;
import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.common.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*loads a user’s data given its username */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // let people login with username or email
        User user = userRepository.findByUsernameOrEmail(s, s)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email " + s));

        if(!user.isEnabled()){
            throw new AppException("You must verify Your email");
        }

        return UserPrincipal.create(user);
    }


    // this method is used by JWTAuthenticationFilter

    public UserDetails loadUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()->
                new UsernameNotFoundException("User not found with id : " + id));

        return UserPrincipal.create(user);
    }
}