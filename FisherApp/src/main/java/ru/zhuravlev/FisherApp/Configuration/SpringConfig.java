package ru.zhuravlev.FisherApp.Configuration;


/* author-->
Sergey Zhuravlev
*/

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.zhuravlev.FisherApp.Configuration.Security.AuthProviderImpl;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomAuthenticationEntryPoint;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetailsService;
import ru.zhuravlev.FisherApp.Configuration.Security.JWTAuthenticationFilter;

@Configuration
@EnableCaching
@EnableMethodSecurity
public class SpringConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthProviderImpl authProvider;

    @Autowired
    public SpringConfig(JWTAuthenticationFilter jwtAuthenticationFilter,CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                        CustomUserDetailsService customUserDetailsService,AuthProviderImpl authProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customUserDetailsService = customUserDetailsService;
        this.authProvider = authProvider;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/api/auth/*").anonymous()
                        .requestMatchers(HttpMethod.GET,"/api/users/*").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
                .userDetailsService(customUserDetailsService)
                .authenticationProvider(authProvider);
        return http.build();
    }
}
