package com.tms.controller;

import com.tms.controller.entity.Controller;
import com.tms.model.entity.NotSentEmail;
import java.io.Serializable;
import javax.faces.bean.RequestScoped;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@RequestScoped
public class NotSentEmailController extends Controller<NotSentEmail> implements Serializable {
    
}
