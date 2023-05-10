package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

}
