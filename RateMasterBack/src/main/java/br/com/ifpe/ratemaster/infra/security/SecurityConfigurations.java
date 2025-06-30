package br.com.ifpe.ratemaster.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))  // referência aqui
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //User
                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/user/update/*").authenticated()
                        // Brand
                        .requestMatchers(HttpMethod.GET, "/api/brand").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/brand/register").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.PUT, "/api/brand/update/{id}").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.DELETE, "/api/brand/delete/{id}").hasRole("BUSINESS")
                        // Category
                        .requestMatchers(HttpMethod.GET, "/api/category").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/category/update/{id}").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/delete/{id}").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.POST, "/api/category/register").hasRole("BUSINESS")
                        //Product
                        .requestMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product/register").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.GET, "/api/product/myProducts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/product/update/{id}").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/delete/{id}").hasRole("BUSINESS")
                        .requestMatchers(HttpMethod.GET, "/api/product/reviewedBy/{Id}").authenticated()
                        //Review
                        .requestMatchers(HttpMethod.GET, "/api/review").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/review/register").authenticated()
                        //Response
                        .requestMatchers(HttpMethod.GET, "/api/response").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/response/register").hasRole("BUSINESS")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
