package com.anisha.UserServiceF;


import com.anisha.UserServiceF.models.Role;
import com.anisha.UserServiceF.models.User;
import com.anisha.UserServiceF.repositories.RoleRepository;
import com.anisha.UserServiceF.repositories.UserRepository;
import com.anisha.UserServiceF.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceIntegrationTest {

        @Autowired
        private UserService userService;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        @Test
        @Transactional
        @Commit
        void addUserWithAdminRole_commitsToDb() {
            // Find the admin role
            Optional<Role> adminRoleOpt = roleRepository.findByRole("ROLE_ADMIN");
            assertTrue(adminRoleOpt.isPresent(), "Admin role must exist in DB");

            // Create and save a new user
            User user = new User();
            user.setEmail("testadmin@gmail.com");
            user.setPassword(bCryptPasswordEncoder.encode("testadmin"));
            user = userRepository.save(user);

            // Assign admin role using the service
            userService.setUserRoles(user.getId(), Collections.singletonList(adminRoleOpt.get().getId()));

            // Fetch user and verify role
            User savedUser = userRepository.findById(user.getId()).orElseThrow();
            assertTrue(savedUser.getRoles().stream().anyMatch(r -> r.getRole().equals("ROLE_ADMIN")));
        }

}