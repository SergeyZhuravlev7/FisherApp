package ru.zhuravlev.FisherApp.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.zhuravlev.FisherApp.Models.User;
import ru.zhuravlev.FisherApp.Services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userService.findByLogin(username);
        if (result.isPresent()) return new UserImpl(result.get());
        throw new BadCredentialsException("Пользователь с таким именем не существует");
    }
}
