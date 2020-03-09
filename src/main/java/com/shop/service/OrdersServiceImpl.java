package com.shop.service;

import com.shop.dao.OrdersDao;
import com.shop.entities.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService{
    
    @Autowired
    OrdersDao odao;
    
    public Orders create(Orders o){
        odao.create(o);
        return o;
    }
}
