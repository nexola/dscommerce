package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.projections.UserDetailsProjection;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsFactory {
    public static List<UserDetailsProjection> createCustomClientUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        UserDetailsImpl userDetails = new UserDetailsImpl(username, "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy", 1L, "ROLE_CLIENT");
        list.add(userDetails);
        return list;
    }

    public static List<UserDetailsProjection> createCustomAdminUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        UserDetailsImpl userDetails = new UserDetailsImpl(username, "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy", 2L, "ROLE_ADMIN");
        list.add(userDetails);
        return list;
    }

    public static List<UserDetailsProjection> createCustomAdminClientUser(String username) {
        List<UserDetailsProjection> list = new ArrayList<>();
        UserDetailsImpl userDetailsAdmin = new UserDetailsImpl(username, "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy", 2L, "ROLE_ADMIN");
        UserDetailsImpl userDetailsClient = new UserDetailsImpl(username, "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy", 1L, "ROLE_CLIENT");
        list.add(userDetailsAdmin);
        list.add(userDetailsClient);
        return list;
    }
}

class UserDetailsImpl implements UserDetailsProjection {

    private String username;
    private String password;
    private Long roleId;
    private String authority;

    public UserDetailsImpl() {}

    public UserDetailsImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
