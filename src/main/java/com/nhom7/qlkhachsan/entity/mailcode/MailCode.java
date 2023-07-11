package com.nhom7.qlkhachsan.entity.mailcode;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
@Table(name="mailcode")
public class MailCode {
    @NotBlank
    @Id
    @Column(nullable = false)
    private String mail;

    @NotBlank
    @Column(nullable = false)
    private int code;

    @CreatedDate
    private Date createdAt;
}
