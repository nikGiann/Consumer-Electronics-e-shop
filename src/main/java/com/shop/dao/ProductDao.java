package com.shop.dao;

import com.shop.entities.Category;
import com.shop.entities.Product;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao implements InterfaceDao<Product> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<Product> findAll() {
        Query q = getSession().createNamedQuery("Product.findAll");
        List<Product> list = q.getResultList();
        return list;
    }

    @Override
    public boolean createOrUpdate(Product p) {
        try {
            getSession().saveOrUpdate(p);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Product> findNewest() {
        Query q = getSession().createNamedQuery("Product.findLimit");
        List<Product> list = q.setMaxResults(5).getResultList();
        return list;
    }

    public List<Product> findSales() {
        Query q = getSession().createNamedQuery("Product.findSales");
        List<Product> list = q.setMaxResults(5).getResultList();
        return list;
    }

    public List<Category> getDetails() {
        Query q = getSession().createNamedQuery("Category.findAll");
        List<Category> list = q.getResultList();
        return list;
    }

    public List<Product> getByCategory(Integer category) {
        Query q = getSession().createQuery("SELECT p FROM Product p WHERE p.category.id= :category AND p.description != '000'");
        // '000' in description meens that this product is not available
        q.setParameter("category", category);
        List<Product> list = q.getResultList();
        return list;
    }
    
    public List<Product> getByPrice(BigDecimal initialPrice, BigDecimal finalPrice, Integer category) {
        Query q = getSession().createQuery("SELECT p FROM Product p WHERE p.price-p.sales BETWEEN :initialPrice AND :finalPrice AND p.category.id= :category AND p.description != '000'");
        q.setParameter("initialPrice", initialPrice);
        q.setParameter("finalPrice", finalPrice);
        q.setParameter("category", category);
        List<Product> list = q.getResultList();
        return list;
    }

        public Product findById(Integer id) {
        return (Product) getSession().get(Product.class, id);
    }
    
    public Product hasSameName (Product product){
        Query q = getSession().createQuery("SELECT p FROM Product p WHERE p.name= :name");
        q.setParameter("name", product.getName());
        List<Product> list = q.getResultList();
        if (list.size()>0){
        return list.get(0);
        }
        return null;
    }
   
    public Product hasSameCode (Product product){
        Query q = getSession().createQuery("SELECT p FROM Product p WHERE p.pcode= :pcode");
        q.setParameter("pcode", product.getPcode());
        List<Product> list = q.getResultList();
        if (list.size()>0){
        return list.get(0);
        }
        return null;
    }
   
    public boolean hasSameNameExceptThisOne (Product product){
      
        Query q = getSession().createNativeQuery("SELECT * FROM product WHERE name= :name and id!= :id");
        q.setParameter("name", product.getName());
        q.setParameter("id", product.getId());
        List<Product> list = q.getResultList();
        if (list.size()>0){
        return true;
        }
        return false;
    }
   
    public boolean hasSameCodeExceptThisOne (Product product){
        Query q = getSession().createNativeQuery("SELECT * FROM product WHERE pcode= :pcode and id!= :id");
        q.setParameter("pcode", product.getPcode());
        q.setParameter("id", product.getId());
        List<Product> list = q.getResultList();
        if (list.size()>0){
        return true;
        }
        return false;
    }
    
    public List<Product> search (String userInput){
        System.out.println("test search************  " + userInput);
        Query q = getSession().createQuery("SELECT p FROM Product p WHERE p.name LIKE :userInput "
                + "or p.pcode LIKE :userInput AND p.description != '000'");
        q.setParameter("userInput", userInput);
        q.setParameter("userInput", "%" + userInput + "%");
        return q.getResultList();
    }
    
    public void setProductUnavailable(Product product){
        product.setDescription("000");
        getSession().saveOrUpdate(product);
    }
}
