package com.myproject.projectmanagement.repositories;

import com.myproject.projectmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
