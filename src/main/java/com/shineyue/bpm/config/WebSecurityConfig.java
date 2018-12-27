package com.shineyue.bpm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.shineyue.bpm.security.CustomUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService customUserService(){
		return new CustomUserService();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(){
		return new PasswordAuthenticationProvider();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.authenticationProvider(authenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/login.html").permitAll()
				.antMatchers("/failure.html").permitAll()
				.antMatchers("/admin/**").permitAll()
				//.antMatchers("/modelEditor/**").hasIpAddress("127.0.0.1")
		 //.antMatchers("/porcess-editor/**").hasIpAddress("127.0.0.1")
						.anyRequest().authenticated().and()
						.formLogin()
							.loginPage("/login")
							.failureUrl("/failure")
							.permitAll()
						.and()
						.logout().permitAll();
	}


}
