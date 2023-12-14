package com.MusicPlayer.MuzikPlear.UserDetailsService;

import com.MusicPlayer.MuzikPlear.Model.User;
import com.MusicPlayer.MuzikPlear.Repository.UserRepository;
import com.MusicPlayer.MuzikPlear.UserDetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User does not exists.");
        }
        return new CustomUserDetails(user);
    }
}
