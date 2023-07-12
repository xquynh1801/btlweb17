package com.nhom7.qlkhachsan.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhom7.qlkhachsan.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailFormatInterceptor implements HandlerInterceptor {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.hasMethodAnnotation(PostMapping.class)) {
                PostMapping postMapping = handlerMethod.getMethodAnnotation(PostMapping.class);
                String[] mappingValues = postMapping.value();
                if (mappingValues.length > 0 && "/register".equals(mappingValues[0])) {
                    // Sử dụng HttpServletRequestWrapper để đọc dữ liệu từ request body
                    HttpServletRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                    BufferedReader bf = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
                    String requestBody = "";
                    String tmp = "";
                    while((tmp=bf.readLine())!=null){
                        requestBody += tmp;
                    }

                    // Parse thông tin từ request body thành đối tượng LoginDTO
                    LoginDTO loginDTO = objectMapper.readValue(requestBody, LoginDTO.class);
                    request.setAttribute("loginDTO", loginDTO);

                    // Kiểm tra định dạng email
                    String email = loginDTO.getFullName();
                    if (!isValidEmail(email)) {
                        // Nếu định dạng email không hợp lệ, trả về lỗi
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Invalid email format");
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        // Kiểm tra định dạng email
        return email != null && email.matches(EMAIL_REGEX);
    }
}
