package com.shop.controller;

import com.shop.entities.*;
import com.shop.service.*;
import com.shop.service.UserService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ProductService service;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    OrdersService ordersService;

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("productId") int id,
            @RequestParam("jspName") String jspName,
            Model m) {
        Product p = service.findById(id);
        System.out.println("testakia **** : " + p.getQuantity());
//        changeQuantity(p);
        addItem(p);
        if (!jspName.equals("products")) {
            return "redirect:/" + jspName;
        }
        return "redirect:/";
    }

    @GetMapping("/showCart")
    public String showCart(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        List<CartItem> cart = getCart();
        m.addAttribute("cart", cart);
        double finalPrice = ProductController.getFinalPrice();
        Double toBeTruncated = new Double(finalPrice);
        Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        m.addAttribute("finalPrice", truncatedDouble);
        return "cart";
    }

    @GetMapping("/nextStepPayment")
    public String nextStepPayment(Model m, RedirectAttributes redirectAttr, @ModelAttribute("user") User user) {
        int countCart = ProductController.getCountCart();
        redirectAttr.addFlashAttribute("user", user);

        if (m.getAttribute("user") == null) {
            m.addAttribute("user", new User());
        }
        m.addAttribute("countCart", countCart);
        List<CartItem> cart = getCart();
        m.addAttribute("cart", cart);
        double finalPrice = 0;
        for (int i = 0; i < cart.size(); i++) {
            finalPrice = finalPrice + (cart.get(i).getProduct().getPrice().doubleValue() - cart.get(i).getProduct().getSales().doubleValue()) * cart.get(i).getQuantity();
        }
        Double toBeTruncated = new Double(finalPrice);
        Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        m.addAttribute("finalPrice", truncatedDouble);
        return "paymentCredentials";
    }

    @PostMapping("/payment/{price}")
    public String payment(Model m, @RequestParam("paymentMethod") String paymentMethod, @ModelAttribute("user") User user,
            @PathVariable("price") BigDecimal price, RedirectAttributes redirectAttr) {
        System.out.println("test price " + price);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId;
        User userCredentials;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            userCredentials = userService.findByUsername(currentUserName);
            userId = userCredentials.getUid();
        } else {
            userCredentials = userService.findById(3);
        }
        LocalDate date = LocalDate.now();
        Orders order = new Orders(user.getCountry(), user.getCity(), user.getAddress(), user.getPostal_code(), user.getTelephone(), user.getEmail(), userCredentials, price, date);
        Orders commitedOrder = ordersService.create(order);
        getSession().setAttribute("order", commitedOrder);
        redirectAttr.addFlashAttribute("order", commitedOrder);
        m.addAttribute("order", commitedOrder);
        getSession().setAttribute("user", user);
        redirectAttr.addFlashAttribute("user", user);
        List<CartItem> cart = getCart();
        getSession().setAttribute("cart", cart);
        redirectAttr.addFlashAttribute("cart", cart);
        if (paymentMethod.equals("cash")) {
            return "redirect:/cart/final";
        }else{
            return "creditCard";//redirect controller meta antistoixi jsp kai meta /final
        }
    }
    

    @GetMapping("/final")
    public String finalOrderController(Model m) {
        Orders order = (Orders) getSession().getAttribute("order");
        m.addAttribute("order", order);
        StringBuilder builder = new StringBuilder();
        String code = "20190000" + order.getId();
        m.addAttribute("code", code);
        User user = (User) getSession().getAttribute("user");
        List<CartItem> cart = (List<CartItem>) getSession().getAttribute("cart");
        builder.append("Dear ").append(user.getLname() + " " + user.getFname() + ",\n\n")
                .append("your order in Geek.com has been submited with number: ").append(code + ". \n\n")
                .append("Order's details:\n\n");
        for (int i = 0; i < cart.size(); i++) {
            builder.append(cart.get(i).getQuantity() + " x " + cart.get(i).getProduct().getName() + "\n");
        }
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        builder.append("\nTotal price: " + finalPrice + "€\n\n")
                .append(order.getAddress() + ", " + order.getPostalCode()).append(" ," + order.getCity())
                .append("\nThank you for choosing Geek.com")
                .append("\n\nAthens, " + order.getSubmitDate() + "\n\n")
                .append("Best regards,\nGeek.com team");
        String messageUser = builder.toString();
        sendEmail(messageUser, order.getEmail(), code);
        getSession().setAttribute("cart", new ArrayList<CartItem>());
        return "orderDone";
    }

    @GetMapping("/user")
    public String getUser(Model m, RedirectAttributes redirectAttr) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userService.findByUsername(currentUserName);
            redirectAttr.addFlashAttribute("user", user);
        }

        return "redirect:/cart/nextStepPayment";
    }

    public boolean addItem(Product p) {
        List<CartItem> cart = getCart();
        if (cart == null) {
            cart = new ArrayList();
            cart.add(new CartItem(p));
            getSession().setAttribute("cart", cart);

        } else {
            for (CartItem c : cart) {
                if (c.getProduct().getId() == p.getId()) {
                    c.addQuantity();
                    getSession().setAttribute("cart", cart);
                    return true;
                }
            }
            cart.add(new CartItem(p));
            getSession().setAttribute("cart", cart);
        }
        return true;
    }

    public int getCountCart() {
        List<CartItem> cart = getCart();
        int count = 0;
        for (CartItem c : cart) {
            count = count + c.getQuantity();
        }
        return count;
    }

    @GetMapping("/cartItem/delete")
    public String deleteCartItem(@RequestParam("productId") int id) {
        List<CartItem> cart = getCart();
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId() == id) {
                if (cart.get(i).getQuantity() > 1) {
                    cart.get(i).setQuantity(cart.get(i).getQuantity() - 1);
                } else {
                    cart.remove(i);
                }
            }
        }
        getSession().setAttribute("cart", cart);
        return "redirect:/cart/showCart";
    }

    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public List<CartItem> getCart() {
        List<CartItem> cartList = (List<CartItem>) getSession().getAttribute("cart");
        if (cartList == null) {
            cartList = new ArrayList<CartItem>();
            getSession().setAttribute("cart", cartList);
        }
        return cartList;
    }

    public void sendEmail(String msgUser, String userMail, String orderCode) {

        final String username = "geekshop.com.info@gmail.com";
        final String password = "123456789!@#$%^&*(";

        Properties prop = new Properties();

        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            //send to customer
            Message messageUser = new MimeMessage(session);
            messageUser.setFrom(new InternetAddress("geekshop.com.info@gmail.com"));
            messageUser.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(userMail));
            messageUser.setSubject("Order Details, Geek Shop");
            messageUser.setText(msgUser);
            Transport.send(messageUser);

            //send to Admin
            Message messageAdmin = new MimeMessage(session);
            messageAdmin.setFrom(new InternetAddress("geekshop.com.info@gmail.com"));
            messageAdmin.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("geekshop.com.info@gmail.com"));

            messageAdmin.setSubject("Geek Shop. You have a new order with order code: " + orderCode);
            messageAdmin.setText(msgUser);
            Transport.send(messageAdmin);

            System.out.println("Email has been sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
