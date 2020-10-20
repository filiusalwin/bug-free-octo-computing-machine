package nl.miwgroningen.se.ch3.bacchux.configuration;


import nl.miwgroningen.se.ch3.bacchux.service.BacchuxUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    BacchuxUserDetailsService bacchuxUserDetailsService;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(bacchuxUserDetailsService);
    }

    //encode password from user
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // roles Barmanager allow to access /all
    // roles Bartender only allow to access /order/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/user/**", "/catalog/**").hasRole("BARMANAGER")
                    .antMatchers("/order/**").hasRole("BARTENDER")
                    .antMatchers("/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/appLogout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
    }

    @Configuration
    public class MyHttpSessionListener implements HttpSessionListener {
        @Override
        public void sessionCreated(HttpSessionEvent event) {
            event.getSession().setMaxInactiveInterval(120 * 60); // 2 hours
        }

        @Override
        public void sessionDestroyed(HttpSessionEvent event) {
            // session destroyed
        }
    }

}
