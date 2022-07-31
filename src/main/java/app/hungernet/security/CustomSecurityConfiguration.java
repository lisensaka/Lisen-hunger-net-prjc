package app.hungernet.security;

import app.hungernet.security.jwtConfigs.JwtAuthenticationEntryPoint;
import app.hungernet.security.jwtConfigs.JwtRequestFilter;
import app.hungernet.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static app.hungernet.constants.ApplicationConstantsConfigs.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(USER_API + "/**", RESTAURANT_API + "/admin-path/**", MENU_API+"/admin-path/**", FOOD_API+"/admin-path/**").hasAuthority("ADMIN")
                .antMatchers(ORDER_API + "/**", RESTAURANT_API + "/all/**").hasAnyAuthority("CLIENT", "RESTAURANT_MANAGER", "ADMIN")
                .antMatchers(MENU_API + "/**", FOOD_API+"/**", RESTAURANT_API + "/**").hasAnyAuthority("RESTAURANT_MANAGER")
                .antMatchers(REGISTRATION_API + "/add/**", LOGIN_API + "/**", FOOD_API+"/allFoods").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/v1/management/v2/api-docs",
                //   LOGIN_API+"/**",
                "/api/v1/management/configuration/ui",
                "/api/v1/management/swagger-resources/**",
                "/api/v1/management/configuration/security",
                "/api/v1/management/swagger-ui.html",
                "/api/v1/management/webjars/**");
    }

}
