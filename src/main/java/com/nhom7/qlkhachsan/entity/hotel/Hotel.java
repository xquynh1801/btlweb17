package com.nhom7.qlkhachsan.entity.hotel;

import com.nhom7.qlkhachsan.entity.BaseEntity;
import com.nhom7.qlkhachsan.entity.rating.Comment;
import com.nhom7.qlkhachsan.entity.rating.Follow;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="hotel")
public class Hotel extends BaseEntity {
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private String phoneNumber;

    private String description;

    @Column(nullable = false)
    private String imagePath;

    @OneToMany(mappedBy = "hotelIsFollowed")
    private Set<Follow> follows;

    @OneToMany(mappedBy = "hotelIsCommented")
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "hotelHasRooms")
    private List<Room> rooms;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
