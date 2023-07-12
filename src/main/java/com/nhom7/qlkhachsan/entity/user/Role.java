package com.nhom7.qlkhachsan.entity.user;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name="role")
public class Role extends BaseEntity implements Serializable {
    private String roleName;
//
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> usersHasRole;
}
