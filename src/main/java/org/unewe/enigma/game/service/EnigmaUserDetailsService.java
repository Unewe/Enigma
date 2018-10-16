package org.unewe.enigma.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unewe.enigma.game.entity.EnigmaRole;
import org.unewe.enigma.game.entity.EnigmaUser;
import org.unewe.enigma.game.repository.EnigmaRoleRepository;
import org.unewe.enigma.game.repository.EnigmaUserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnigmaUserDetailsService implements UserDetailsService {

    @Autowired
    private EnigmaUserRepository enigmaUser;
    @Autowired
    private EnigmaRoleRepository enigmaRole;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        EnigmaUser user = enigmaUser.findByUsername(s);

        if(user == null) {
            System.out.println("User " + s + " not found");
            throw new UsernameNotFoundException("User " + s + " not found");
        }

        EnigmaRole role = enigmaRole.findByUserId(user.getRoleId());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));

        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        System.out.println(userDetails);
        return userDetails;
    }
}
