package ru.zhuravlev.FisherApp.Configuration;


/* author-->
Sergey Zhuravlev
*/

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(r ->r.requestMatchers("api/auth/**").permitAll())
                .authorizeHttpRequests(r -> r.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> basic.init(http));
        return http.build();
    }
}
