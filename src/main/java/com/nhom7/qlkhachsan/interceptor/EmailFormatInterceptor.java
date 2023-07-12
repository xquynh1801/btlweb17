package com.nhom7.qlkhachsan.interceptor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailFormatInterceptor implements HandlerInterceptor {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Kiểm tra định dạng email
        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        String email = extractEmailFromRequestBody(requestBody);
        boolean isValidEmail = validateEmailFormat(email);
        if (!isValidEmail) {
            // Nếu email không hợp lệ, chuyển hướng người dùng đến trang lỗi
//            response.sendRedirect("/error");
            System.out.println("===========>ValidEmail");
            return false; // Dừng xử lý các interceptor và controller khác
        }
        return true; // Cho phép đi tiếp đến interceptor và controller tiếp theo
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Không cần thực hiện xử lý sau khi controller được xử lý
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Không cần thực hiện xử lý sau khi view đã được render hoàn thành
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean validateEmailFormat(String email) {
        if (isValidEmail(email)) {
            return false;
        }
        return true;
    }
    private String extractEmailFromRequestBody(String requestBody) {
        // Trích xuất dữ liệu email từ body của yêu cầu
        // Tùy thuộc vào cấu trúc body của yêu cầu, bạn có thể sử dụng các thư viện như Gson hoặc Jackson để phân tích dữ liệu JSON hoặc XML.
        // Trong ví dụ này, giả sử body của yêu cầu là một chuỗi JSON với thuộc tính "email"
        if (!StringUtils.isEmpty(requestBody)) {
            // Giả sử sử dụng thư viện Gson để phân tích JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
            if (jsonObject.has("fullName")) {
                return jsonObject.get("fullName").getAsString();
            }
        }
        return null;
    }
}
