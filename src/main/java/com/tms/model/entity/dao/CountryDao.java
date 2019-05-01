/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Country;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public interface CountryDao extends EntityDao<Country> {

    public List<Country> findByName(final String name);
    
}
