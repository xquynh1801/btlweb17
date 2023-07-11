package com.nhom7.qlkhachsan.controller;

import com.nhom7.qlkhachsan.entity.UserMessage;
import com.nhom7.qlkhachsan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    @Value("${spring.mail.username}")
    private String mailServer;
    @Value("${web.login.url}")
    private String loginPath;


    private final UserService userService;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @PostMapping("/register")
    public String register(@RequestBody UserMessage userMessage){
        try{
            String userId = UUID.randomUUID().toString();
            userMessage.setUserId(userId);
            userMessage.setCreatedDate(new Date());
            String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.SECOND, 120);

            userMessage.setOtpSignUp(otp);
            userMessage.setExpiredDate(c.getTime());
            userService.saveToRedis(userMessage);
            sendConfirmLink(userMessage);
            return "Confirm email";
        }catch (Exception e){
            log.error(e.getMessage());
            return "Can't register, some thing wrong";
        }
    }

    public void sendConfirmLink(UserMessage user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("%s/confirm-user/%s", "http://localhost:8080", user.getUserId());
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(mailServer);
        helper.setTo(user.getEmail());
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        mailSender.send(message);
    }

    @GetMapping(path = "/confirm-user/{secretKey}/{otp}")
    public String confirmUser(@PathVariable String secretKey, @PathVariable String otp, HttpServletResponse response) throws IOException {

        try {
            UserMessage user = userService.confirmSecretKey(secretKey);
            if(otp.equals(user.getOtpSignUp())) {
                log.info("Done");
                return "Verify success";
            }
            else{
                log.info("Not match");
                return "OTP not match";
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "Verify error";
        }
    }

    @GetMapping(path = "/get-all-user-redis")
    public Object confirmUser()  {
        try {
            return userService.getUserInRedis();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
