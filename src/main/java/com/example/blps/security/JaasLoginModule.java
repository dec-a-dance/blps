package com.example.blps.security;

import com.example.blps.entities.AuthToken;
import com.example.blps.repositories.AuthTokenRepository;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class JaasLoginModule implements LoginModule {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthTokenRepository authTokenRepository;
    private Subject subject;
    private CallbackHandler callbackHandler;

    private String username;
    private boolean loginSucceeded;

    @Override
    public void initialize(
            Subject subject,
            CallbackHandler callbackHandler,
            Map<String, ?> sharedState,
            Map<String, ?> options){
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.authTokenRepository = (AuthTokenRepository) options.get("authTokenRepository");
    }

    public boolean login(){
        NameCallback nameCallback = new NameCallback("username");
        PasswordCallback passwordCallback = new PasswordCallback("password", false);

        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            username = nameCallback.getName();
            String password = String.valueOf(passwordCallback.getPassword());
            AuthToken authToken = authTokenRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("User not found by email")
            );
            loginSucceeded = passwordEncoder.matches(password, authToken.getPassword());
        } catch (UsernameNotFoundException e) {
            log.warn("User with name = {} was not found during authentication", username);
            loginSucceeded = false;
        } catch (IOException | UnsupportedCallbackException e) {
            log.error("Error occurred during invocation of callback handler = {}", e.getMessage());
            loginSucceeded = false;
        }

        return loginSucceeded;
    }

    @Override
    public boolean commit() throws LoginException {
        if(!loginSucceeded){
            return false;
        }
        if(username == null){
            throw new LoginException("Username is null");
        }
        Principal principal = new UserPrincipal(username);
        subject.getPrincipals().add(principal);
        return true;
    }

    @Override
    public boolean abort(){
        return false;
    }

    @Override
    public boolean logout(){
        return false;
    }
}
