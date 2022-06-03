package com.example.blps.delegates;

import com.example.blps.dto.LoginDTO;
import com.example.blps.dto.LoginResponse;
import com.example.blps.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class LoginDelegate implements JavaDelegate {
    private final AuthService authService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        LoginResponse loggedIn = authService.login(new LoginDTO(username, password));
        delegateExecution.setVariable("login_success", (loggedIn==null));
        if(loggedIn!=null) {
            delegateExecution.setVariable("username_storage", username);
        }
    }
}
