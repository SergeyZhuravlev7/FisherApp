package ru.zhuravlev.FisherApp.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationManager {

    private final AuthProviderImpl authProvider;

    @Autowired
    public AuthManager(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authProvider.supports(authentication.getClass())) return authProvider.authenticate(authentication);
        else throw new AuthenticationCredentialsNotFoundException("Аутентификация не поддерживается");
    }
}
