package demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author sudhir
 *         Date:7/11/16
 *         Time:5:48 PM
 *         Project:demo
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/triggerEmail").hasAuthority("photoReviewers")
                .antMatchers("/approve").hasAuthority("photoReviewers")
                .antMatchers("/").authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
    }
}
