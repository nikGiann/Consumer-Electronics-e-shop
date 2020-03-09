package com.shop.dao;

import com.shop.entities.User;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public User findByUsername(String username) {
        Query q = getSession().createQuery("SELECT u FROM User u WHERE u.username=:name");
        q.setParameter("name", username);
        User user;
        List <User> list = q.getResultList();
        if( list.size() > 0){
            user = (User)q.getResultList().get(0);
        }else{
            user = null;
        }
//        try{
//        }catch(NoResultException e){
//            System.out.println("There is no result");
//            user = null;
//        }
        return user;
    }

    @Override
    public void save(User user) {
        getSession().save(user);
    }

    @Override
    public User findByEmail(String email) {
         Query q = getSession().createQuery("SELECT u FROM User u WHERE u.username=:email");
        q.setParameter("email", email);
        User user = null;
        try{
            user = (User)q.getSingleResult();
        }catch(NoResultException e){
            System.out.println("There is no result");
            user = null;
        }
        return user;
    }

    @Override
    public User findById(Integer id) {
        Query q = getSession().createQuery("SELECT u FROM User u WHERE u.uid = :id");
        q.setParameter("id", id);
        return (User)q.getResultList().get(0);
    }
    
}
