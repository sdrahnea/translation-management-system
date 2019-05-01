package com.tms.controller.entity;

import com.tms.model.entity.PaymentMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="request")
public class PaymentMethodController extends Controller<PaymentMethod>{
    
}
