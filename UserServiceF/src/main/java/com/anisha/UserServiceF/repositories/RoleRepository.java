package com.anisha.UserServiceF.repositories;

import com.anisha.UserServiceF.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    List<Role> findAllByIdIn(List<Long> roleIds);
    Optional<Role> findByRole( String role );
}