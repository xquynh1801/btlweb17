package com.nhom7.qlkhachsan.app.entity.rating;

import com.nhom7.qlkhachsan.app.entity.hotel.Hotel;
import com.nhom7.qlkhachsan.app.entity.BaseEntity;
import com.nhom7.qlkhachsan.app.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name="comment")
public class Comment extends BaseEntity  implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User userComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id", nullable = false)
    private Hotel hotelIsCommented;

//    @NotBlank
//    @NotNull
    @Size(max = 100)
    private String body;
}
