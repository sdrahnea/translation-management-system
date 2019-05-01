/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.controller.entity;

import com.tms.model.entity.ProjectType;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="request")
public class ProjectTypeController extends Controller<ProjectType>{
    
}
