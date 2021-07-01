package it.itdistribuzione.gilda.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import it.itdistribuzione.gilda.spring.component.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(customAuthenticationProvider);
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/sec/login").permitAll()
        .antMatchers("/sec/loginFailure").permitAll()
        .antMatchers("/html/HomePubblicaPage.html").permitAll()
        .antMatchers("/v2/api-docs").permitAll()
        .antMatchers("/api/**").authenticated()
        .antMatchers("/html/**").authenticated()
        .antMatchers("/**").permitAll()
        .and()
        .formLogin()
        .loginPage("/html/HomePubblicaPage.html")
        .loginProcessingUrl("/sec/login")
        .defaultSuccessUrl("/html/HomePrivataPage.html")
        .failureUrl("/html/HomePubblicaPage.html?Error=true")
        .and()
        .logout()
        .logoutUrl("/sec/logout")
        .logoutSuccessUrl("/html/HomePubblicaPage.html")
        .deleteCookies("JSESSIONID");
  }

}