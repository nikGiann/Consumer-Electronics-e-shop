package com.shop.dao;

import com.shop.entities.Role;
import java.util.List;

public interface RoleDao {

    public List<Role> findAll();

    public Role findById(Integer id);
    
}
