package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.models.ClientUser;

import java.util.List;

public interface ClientUserService {
    ClientUser saveUser(ClientUser user);
    ClientUser getUser(String username);
    List<ClientUser> getUsers();
    boolean checkUser(String username);
}
