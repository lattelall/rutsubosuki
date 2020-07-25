package com.rutsubosuki.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsManager userDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // UserDetailServiceとPasswordEncorderを指定
        auth.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure a new SecurityChainFilter.
        http.authorizeRequests()
            .antMatchers("/").permitAll()//permitAll ->loginなしでアクセス可
            .antMatchers("/*").permitAll()
            .antMatchers("/login*").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/signup").permitAll()
            .antMatchers("/register").permitAll()
            .antMatchers("/article").permitAll() //記事一覧用
            .antMatchers("/article/*").permitAll()
            .antMatchers("/tag").permitAll()
            .antMatchers("/tag/*").permitAll()
            .antMatchers("/image").permitAll()
            .antMatchers("/image/*").permitAll()
            .antMatchers("/upload").permitAll()
            .antMatchers("/upload/*").permitAll()
            .antMatchers("/index").permitAll()
            .antMatchers("/imageupload").permitAll()
            .anyRequest().authenticated();
        http.formLogin()
            .loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .successForwardUrl("/")
            .failureForwardUrl("/login-fail?error")
            .permitAll();
        http.logout()
            .logoutSuccessUrl("/")
            .permitAll();
        //4.認証設定の追加

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Configure a new SecurityChainFilter
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}