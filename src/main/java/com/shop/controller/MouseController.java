package com.shop.controller;

import com.shop.entities.*;
import com.shop.service.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mouse")
public class MouseController {
    
    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MouseService serviceMouse;

    List<Mouse> allMouses;
    List<MouseManufacturer> manufacturers;
    List<MouseType> types;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;
    
    public String showMouse(Model m, List<Product> productList, List<Mouse> mouseList) {
        m.addAttribute("productList", productList);
        m.addAttribute("mouseList", mouseList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product()); //????????????????????????????????????????
        }
        if (manufacturers == null) {
            manufacturers = serviceMouse.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (types == null) {
            types = serviceMouse.findAllTypes();
        }
        m.addAttribute("types", types);
        return "mouse";
    }

    @GetMapping
    public String displayAllMouse(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(9);
        }
        if (allMouses == null) {
            allMouses = serviceMouse.getAll();
        }
        return showMouse(m, productsByCategory, allMouses);
    }
    
    //_______________________________FILTERS____________________________________
    @GetMapping("/price/{initial}/{final}")
    public String getByPrice(Model m,
            @PathVariable("initial") BigDecimal initialPrice,
            @PathVariable("final") BigDecimal finalPr) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 9);
        List<Integer> listIds = ProductIdByPrice(productsByPrice);
        List<Mouse> mouseList = mouseSort(listIds);
        return showMouse(m, productsByPrice, mouseList);
    }
    
    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Mouse> mouseList = serviceMouse.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < mouseList.size(); i++) {
            listIds.add(mouseList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMouse(m, productList, mouseList);
    }
    
    @GetMapping("/type/{Id}")
    public String getByType(Model m,
            @PathVariable("Id") Integer Id) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Mouse> mouseList = serviceMouse.findByType(Id);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < mouseList.size(); i++) {
            listIds.add(mouseList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMouse(m, productList, mouseList);
    }
    
    public List<Mouse> mouseSort(List<Integer> listIds) {
        List<Mouse> concreteMouse = new ArrayList();
        for (int i = 0; i < allMouses.size(); i++) {
            if (listIds.contains(allMouses.get(i).getId())) {
                concreteMouse.add(allMouses.get(i));
            }
        }
        return concreteMouse;
    }

    public List<Product> productSort(List<Integer> listIds) {
        List<Product> concreteProducts = new ArrayList();
        for (int i = 0; i < productsByCategory.size(); i++) {
            if (listIds.contains(productsByCategory.get(i).getId())) {
                concreteProducts.add(productsByCategory.get(i));
            }
        }
        return concreteProducts;
    }
    
    public List<Integer> ProductIdByPrice(List<Product> productsByPrice) {
        List<Integer> listIds = new ArrayList(); //Ids of products by category
        for (int i = 0; i < productsByPrice.size(); i++) {
            listIds.add(productsByPrice.get(i).getId());
        }
        return listIds;
    }
    
    //___________________________________SEE MORE INFO__________________________________________
    @GetMapping("/information/{Id}")
    public String viewMore(Model m,
            @PathVariable("Id") Integer productId){
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        Product product = service.findById(productId);
        product.setId(productId);
        Mouse mouse = serviceMouse.findById(productId);
        return showOneMouseWithInfos(m, product, mouse);
    }
    
    public String showOneMouseWithInfos(Model m, Product product, Mouse mouse){
        m.addAttribute("product", product);
        m.addAttribute("mouseType", mouse.getType());
        m.addAttribute("mouseManufacturer", mouse.getManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if(productsByCategory==null){
            productsByCategory = service.getByCategory(9);
        }
        int listSize = productsByCategory.size();
        int a;
        if(listSize>=5){   
            a = 4;          // Theloume 4 proionta
        }else{
            a = listSize-1; /*se periptosi omos pou iparxoun 4 i ligotero proionta,
            afairoume 1 proion (Auto pou fainetai sti selide me infos)*/
        }
        for(int i=0; i<a; i++){
            Product p = new Product();
            do{
               index = random.nextInt((productsByCategory.size()-1) + 1);
               p = productsByCategory.get(index);
            }while(listOfProducts.contains(p) || p.getId()==product.getId());
            listOfProducts.add(p);
        }
        m.addAttribute("listOfProducts", listOfProducts);
        return "mouseProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductMouse(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("type") Integer type,
            @RequestParam("pricee") String price,
            @RequestParam("ammountt") String ammount,
            @RequestParam("discountt") String discount,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("pricee", price);
        redirectAttrs.addFlashAttribute("ammountt", ammount);
        redirectAttrs.addFlashAttribute("discountt", discount);
        redirectAttrs.addFlashAttribute("product", product); // ksanadinoume sto model to simplirwmeno pleon product etsi wste an petaksei kapoio error na ksanagirisei stin forma exontas ta stoixeia tou product simplirwmena
        boolean isUpdate = false;
        if (product.getId() != null) {
            isUpdate = true;
        }
        if (isUpdate) { // katalavainei an exei patithei to update
            if (service.hasSameNameExceptThisOne(product)) {
                String msg = "There is another product with this name";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/mouse";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/mouse";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/mouse";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/mouse";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/mouse";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/mouse";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/mouse";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/mouse";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/mouse";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/mouse";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/mouse";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/mouse";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/mouse";
                }
            }
            Category category = categoryService.findById(1); // find category from id
            product.setCategory(category); // add category to product
            try {

                int productId = service.createOrUpdate(product).getId();//add product to db and grab the id  //  ___________________CREATE__________________
                if (isUpdate) {
                    productsByCategory.set(indexOfProduct, product);
                } else {
                    productsByCategory.add(product);  //****************prostheto to product sti lista****************************
                }

                if (!file.getContentType().equals("application/octet-stream")) {
                    addPhoto(productId, file); // add photo to Tomcat's folder 
                }
                product.setId(productId);// insert product's id from db into this product
                MouseManufacturer mouseManufacturer = serviceMouse.findByManufacturerId(manufacturer); // find manufacturer from id
                MouseType mouseType = serviceMouse.findByTypeId(type);
                Mouse mouse = new Mouse(productId, mouseManufacturer, mouseType, product); // create product's additional info package(unique for each categoty) 
                boolean mouseInsert = serviceMouse.saveOrUpdate(mouse); //save this package to db
                if(isUpdate){
                    Mouse t = serviceMouse.findById(productId);
                    index = allMouses.indexOf(t);
                    allMouses.set(index, mouse);
                }
                if (mouseInsert) {                             //*************************************************************************************************** 
                    allMouses.add(mouse);
                    price = null;
                    ammount = null;
                    discount = null;
                    
                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/mouse";
                } else {
                    String msg = "Something went wrong on inserting mouse's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/mouse";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/mouse";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/mouse";
        }
    }

    @GetMapping("/update")
    public String updateMouseForm(Model m,
            @RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttrs) {
        Product product = service.findById(productId);
        String price = product.getPrice().toString();
        String ammount = product.getQuantity().toString();
        String discount = product.getSales().toString();
        redirectAttrs.addFlashAttribute("product", product);
        redirectAttrs.addFlashAttribute("pricee", price);
        redirectAttrs.addFlashAttribute("ammountt", ammount);
        redirectAttrs.addFlashAttribute("discountt", discount);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(9);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/mouse";
    }

    public String addPhoto(int imgName, MultipartFile file) {
        String tomcatBase = System.getProperty("catalina.base");
        try {
            String uploadDir = tomcatBase + "/webapps/images";
            File transferFile = new File(uploadDir + "/" + imgName + ".jpg");
            file.transferTo(transferFile);
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/delete")
    public String deleteTower(Model m,
            @RequestParam("productId") Integer productId,
            RedirectAttributes redirectAttrs) {
        Product product = service.findById(productId);
        service.setProductUnavailable(product);
        Mouse mouse = serviceMouse.findById(productId);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(9);
        }
        if (allMouses == null) {
            allMouses = serviceMouse.getAll();
        }
        allMouses.remove(mouse);
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/mouse";
    }
}
