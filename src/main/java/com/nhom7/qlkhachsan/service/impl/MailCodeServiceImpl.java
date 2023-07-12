package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.entity.mailcode.MailCode;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.MailCodeRepository;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.HotelService;
import com.nhom7.qlkhachsan.service.MailCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailCodeServiceImpl implements MailCodeService {
    @Autowired
    private MailCodeRepository mailCodeRepository;

    @Override
    public MailCode createMailCode(MailCode mailCode) {
        return mailCodeRepository.save(mailCode);
    }

    @Override
    public MailCode findByMail(String mail) {
        return mailCodeRepository.findByMail(mail);
    }
}
