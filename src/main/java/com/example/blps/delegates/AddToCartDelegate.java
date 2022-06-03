package com.example.blps.delegates;

import com.example.blps.dto.AddToCartDTO;
import com.example.blps.services.ShoppingService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class AddToCartDelegate implements JavaDelegate {
    private final ShoppingService shoppingService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username_storage");
        long product = (Long) delegateExecution.getVariable("productId");
        long count = (Long) delegateExecution.getVariable("count");

        boolean added = shoppingService.addToCart(product, username, count);

        delegateExecution.setVariable("addToCart_success", added);
    }
}
