package com.nhom7.qlkhachsan.app.database.repository;

import com.nhom7.qlkhachsan.app.entity.dto.RoleDTO;
import com.nhom7.qlkhachsan.app.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);

    @Query(value = "select role.role_name as name from user_role join role on role.id=user_role.role_id where user_id=?1", nativeQuery = true)
    List<RoleDTO> getRolesByUserId(long userID);
}
