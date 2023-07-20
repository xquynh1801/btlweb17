package com.nhom7.qlkhachsan.app.database.repository;

import com.nhom7.qlkhachsan.app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByFullName(String fullName);
}
