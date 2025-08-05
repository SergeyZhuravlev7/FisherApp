package ru.zhuravlev.FisherApp.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findByLogin(username);
        if (optionalUser.isPresent()) return new CustomUserDetails(optionalUser.get());
        throw new BadCredentialsException("Учетные данные не верны.");
    }
}
