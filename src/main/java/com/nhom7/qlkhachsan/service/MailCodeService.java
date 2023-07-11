package com.nhom7.qlkhachsan.service;

import com.nhom7.qlkhachsan.entity.mailcode.MailCode;
import com.nhom7.qlkhachsan.entity.user.User;

public interface MailCodeService{
    MailCode createMailCode(MailCode mailCode);
    MailCode findByMail(String mail);
}
