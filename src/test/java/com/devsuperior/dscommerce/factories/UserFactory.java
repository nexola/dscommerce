package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {
    public static User createUserClient() {
         User user = new User(1L, "Maria", "maria@gmail.com", "11950765468", LocalDate.parse("2001-07-25"), "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy");
         user.getRoles().add(new Role(1L, "ROLE_CLIENT"));
         return user;
    }

    public static User createUserAdmin() {
        User user = new User(2L, "Alex", "alex@gmail.com", "119777777", LocalDate.parse("1987-12-13"), "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy");
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username) {
        User user = new User(id, "Maria", username, "11950765468", LocalDate.parse("2001-07-25"), "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy");
        user.getRoles().add(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username) {
        User user = new User(id, "Alex", username, "119777777", LocalDate.parse("1987-12-13"), "$2a$10$XOTxuTWaItLCm8WZTbKJkuzrIhgIy0ETwhgWRqF.CksFVjFOUeBqy");
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
}
