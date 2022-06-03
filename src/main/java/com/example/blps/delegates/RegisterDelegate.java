package com.example.blps.delegates;

import com.example.blps.dto.RegisterDTO;
import com.example.blps.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named
@RequiredArgsConstructor
public class RegisterDelegate implements JavaDelegate {
    final AuthService authService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        String name  = (String) delegateExecution.getVariable("name");
        String surname = (String) delegateExecution.getVariable("surname");
        String patronimic = (String) delegateExecution.getVariable("patronimic");
        String email = (String) delegateExecution.getVariable("email");
        String phoneNumber = (String) delegateExecution.getVariable("phoneNumber");
        String address = (String) delegateExecution.getVariable("address");

        RegisterDTO dto = new RegisterDTO(username, password, name, surname, patronimic, email, phoneNumber, address);

        boolean registered = authService.saveUser(dto);

        delegateExecution.setVariable("register_success", registered);
    }
}
