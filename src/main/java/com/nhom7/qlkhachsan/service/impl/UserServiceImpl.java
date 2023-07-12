package com.nhom7.qlkhachsan.service.impl;

import com.nhom7.qlkhachsan.dto.request.RegisterRequest;
import com.nhom7.qlkhachsan.entity.user.Role;
import com.nhom7.qlkhachsan.entity.user.User;
import com.nhom7.qlkhachsan.repository.RoleRepository;
import com.nhom7.qlkhachsan.repository.UserRepository;
import com.nhom7.qlkhachsan.service.MailService;
import com.nhom7.qlkhachsan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.Duration;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${spring.mail.username}")
    private String mailServer;
    @Value("${web.login.url}")
    private String loginPath;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Autowired
    private MailService mailService;

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void confirmSecretKey(String secretKey, String otp) {
        RegisterRequest userInRedis = (RegisterRequest) redisTemplate.opsForValue().get(secretKey);
        if(userInRedis == null){
            log.error("OTP expired");
        }
        if(otp.equals(userInRedis.getOtpSignUp())) {
            log.info("Verify success, save user to DB");
            User user = User.builder()
                    .username(userInRedis.getUsername())
                    .fullName(userInRedis.getFullName())
                    .age(userInRedis.getAge())
                    .password(new BCryptPasswordEncoder().encode(userInRedis.getPassword()))
                    .identityCardNumber(userInRedis.getIdentityCardNumber())
                    .phoneNumber(userInRedis.getPhoneNumber())
                    .build();

            Role role = roleRepository.findByRoleName("ROLE_USER");
            HashSet roles = new HashSet();
            roles.add(role);
            user.setRoles(roles);

            createUser(user);
        }else{
            log.error("OTP not match");
        }
    }

    public void saveToRedis(RegisterRequest request){
        redisTemplate.opsForValue().set(request.getUsername(),request, Duration.ofMinutes(5));
        System.out.println(request);
    }

    public List<User> getUserInRedis(){
        return redisTemplate.opsForHash().values("User_register");
    }

    @Override
    public void registerUser(RegisterRequest request) throws MessagingException {
        String otp= new DecimalFormat("100000").format(new Random().nextInt(999999));
        request.setOtpSignUp(otp);
        saveToRedis(request);
//        sendConfirmLink(request);
        mailService.sendMail(request.getUsername(), request.getFullName(), Integer.parseInt(otp));
    }

    // use when send verify link
    public void sendConfirmLink(RegisterRequest user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        String linkConfirm = String.format("%s/confirm-user/%s", "http://localhost:8080", user.getUsername());
        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(mailServer);
        helper.setTo(user.getUsername());
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
