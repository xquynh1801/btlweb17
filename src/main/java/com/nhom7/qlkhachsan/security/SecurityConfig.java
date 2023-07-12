package com.nhom7.qlkhachsan.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().disable();
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/**", "/product/**","/login","/logout","/signup", "/js/**", "/css/**", "/media/**", "/scss/**", "/vendor/**")
                .permitAll();
        http.authorizeRequests().antMatchers("/auth/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/reservation/**", "/hotel/**", "/my_reservation/**").access("hasAnyRole('ROLE_MANAGER','ROLE_USER')");

        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_MANAGER')");

        http.authorizeRequests()
                .antMatchers("/","/login","/logout","/signup")
                .permitAll();

        //http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

//        http.authorizeRequests().and()
//                .formLogin()
//                .loginPage("/login")
//                .failureUrl("/login?fail=true")
//                .and().logout().logoutSuccessUrl("/");

        http.authorizeRequests().and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?fail=true")
                .and().logout().logoutSuccessUrl("/");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()   //let redis handle session creation
                .csrf().disable().cors().and()
                .requestCache().disable().exceptionHandling().and()                         //prevent exception creating duplicate session
                .authorizeRequests().anyRequest().authenticated().and()                     //all endpoints need auth
                .exceptionHandling().authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    CustomUserDetaisService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
