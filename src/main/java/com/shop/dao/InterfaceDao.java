package com.shop.dao;

import java.util.List;

public interface InterfaceDao<T> {

    List<T> findAll();

    public boolean createOrUpdate(T t);
    
    public T findById(Integer id); 

}
