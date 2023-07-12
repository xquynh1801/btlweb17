package com.nhom7.qlkhachsan.repository;

import com.nhom7.qlkhachsan.entity.mailcode.MailCode;
import com.nhom7.qlkhachsan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailCodeRepository extends JpaRepository<MailCode,String> {
    MailCode findByMail(String mail);
}
