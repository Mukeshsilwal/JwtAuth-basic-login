package com.security.repository;

import java.util.Optional;

import com.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.security.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

}
