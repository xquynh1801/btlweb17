	package com.nhom7.qlkhachsan.entity.user;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import com.nhom7.qlkhachsan.entity.hotel.BookingRoom;
import com.nhom7.qlkhachsan.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.entity.rating.Comment;
import com.nhom7.qlkhachsan.entity.rating.Follow;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="user")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    private String phoneNumber;

    @Column(nullable = false)
    private String identityCardNumber;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name="user_role", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "userFollow")
    private Set<Follow> follows;

    @OneToMany(mappedBy = "userComment")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "userBook")
    private Set<BookingRoom> bookingRoomSet;

    @OneToMany(mappedBy = "owner")
    private Set<Hotel> hotels;
}
