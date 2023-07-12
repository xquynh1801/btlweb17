package com.nhom7.qlkhachsan.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        boolean isAuthenticated = checkAuthentication(request);
        if (!isAuthenticated) {
            // Nếu chưa đăng nhập, chuyển hướng người dùng đến trang đăng nhập
            response.sendRedirect("/login");
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

    private boolean checkAuthentication(HttpServletRequest request) {
        // Thực hiện kiểm tra xác thực, ví dụ: kiểm tra session, token, cookie, etc.
        // Trả về true nếu người dùng đã đăng nhập, false nếu chưa
        return true;
    }
}
