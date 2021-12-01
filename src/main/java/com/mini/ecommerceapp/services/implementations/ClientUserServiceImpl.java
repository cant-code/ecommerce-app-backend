package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.ClientUser;
import com.mini.ecommerceapp.repository.ClientUserRepository;
import com.mini.ecommerceapp.services.ClientUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ClientUserServiceImpl implements ClientUserService, UserDetailsService {
    private final ClientUserRepository clientUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientUserServiceImpl(ClientUserRepository clientUserRepository, PasswordEncoder passwordEncoder) {
        this.clientUserRepository = clientUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientUser saveUser(ClientUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return clientUserRepository.save(user);
    }

    @Override
    public ClientUser getUser(String username) {
        return clientUserRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public List<ClientUser> getUsers() {
        return clientUserRepository.findAll();
    }

    @Override
    public boolean checkUser(String username) {
        return clientUserRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        ClientUser user = getUser(s);
        Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
