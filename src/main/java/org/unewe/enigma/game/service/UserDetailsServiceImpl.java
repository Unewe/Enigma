package org.unewe.enigma.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unewe.enigma.game.dao.AppRoleDao;
import org.unewe.enigma.game.dao.AppUserDao;
import org.unewe.enigma.game.entity.AppUser;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private AppRoleDao appRoleDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser appUser = appUserDao.findUserAccount(userName);

        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        System.out.println("Found User: " + appUser);

        List<String> roles = appRoleDao.getRoles(appUser.getUserId());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if(roles!=null) {
            for(String role : roles) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantedAuthorities.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), appUser.getEncrytedPassword(), grantedAuthorities);
        System.out.println(userDetails);
        return userDetails;
    }
}
