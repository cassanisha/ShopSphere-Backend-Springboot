package com.anisha.UserServiceF.services;


import com.anisha.UserServiceF.dtos.UserDto;
import com.anisha.UserServiceF.models.Role;
import com.anisha.UserServiceF.models.User;
import com.anisha.UserServiceF.repositories.RoleRepository;
import com.anisha.UserServiceF.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        System.out.println("I got the request");
        return new UserDto();
//        Optional<User> userOptional = userRepository.findById(userId);
//
//        if (userOptional.isEmpty()) {
//            return null;
//        }
//
//        return UserDto.from(userOptional.get());
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(new HashSet<>(roles));

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
}