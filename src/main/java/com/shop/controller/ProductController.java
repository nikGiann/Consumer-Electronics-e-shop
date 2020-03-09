package com.shop.controller;

import com.shop.entities.*;
import com.shop.service.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping
    public String showProducts(Model m) {
        List<Product> listNewest = service.getNewest();
        List<Product> listSales = service.findSales();
        int countCart = getCountCart();
        m.addAttribute("latestProducts", listNewest);
        m.addAttribute("salesProducts", listSales);
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        return "products";
    }

    public static int getCountCart() {
        List<CartItem> cart = getCart();
        int count = 0;
        for (CartItem c : cart) {
            count = count + c.getQuantity();
        }
        return count;
    }

    public static double getFinalPrice() {
        List<CartItem> cart = getCart();
        double finalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            finalPrice = finalPrice + (cart.get(i).getProduct().getPrice().doubleValue() - cart.get(i).getProduct().getSales().doubleValue()) * cart.get(i).getQuantity();
        }
        Double toBeTruncated = new Double(finalPrice);
        Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return truncatedDouble;
    }

    public static List<CartItem> getCart() {
        List<CartItem> cartList = (List<CartItem>) getSession().getAttribute("cart");
        if (cartList == null) {
            cartList = new ArrayList<CartItem>();
            getSession().setAttribute("cart", cartList);
        }
        return cartList;
    }

    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    @GetMapping("/search")
    public String search(Model m, @RequestParam("userInput") String userInput) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Product> list = service.search(userInput);
        m.addAttribute("list", list);
        m.addAttribute("userInput");
        return "searchResults";
    }
}
