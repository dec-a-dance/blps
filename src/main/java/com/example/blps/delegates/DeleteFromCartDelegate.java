package com.example.blps.delegates;


import com.example.blps.services.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class DeleteFromCartDelegate implements JavaDelegate {
    private final ShoppingService shoppingService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username_storage");
        long product = (Long) delegateExecution.getVariable("productId");

        int deleted = shoppingService.deleteFromCart(username, product);
        boolean result = deleted != 0;

        delegateExecution.setVariable("deleteFromCart_success", result);
    }
}
