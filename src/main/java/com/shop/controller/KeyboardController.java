package com.shop.controller;

import static com.shop.controller.GpuController.productsByCategory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shop.entities.*;
import com.shop.service.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/keyboard")
public class KeyboardController {
    
    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    KeyboardService serviceKeyboard;

    List<Keyboard> allKeyboards;
    List<KeyboardManufacturer> manufacturers;
    List<KeyboardType> types;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;
    
    public String showKeyboard(Model m, List<Product> productList, List<Keyboard> keyboardList) {
        m.addAttribute("productList", productList);
        m.addAttribute("keyboardList", keyboardList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product()); //????????????????????????????????????????
        }
        if (manufacturers == null) {
            manufacturers = serviceKeyboard.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (types == null) {
            types = serviceKeyboard.findAllTypes();
        }
        m.addAttribute("types", types);
        return "keyboard";
    }

    @GetMapping
    public String displayAllKeyboard(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(10);
        }
        if (allKeyboards == null) {
            allKeyboards = serviceKeyboard.getAll();
        }
        return showKeyboard(m, productsByCategory, allKeyboards);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 10);
        List<Integer> listIds = ProductIdByPrice(productsByPrice);
        List<Keyboard> keyboardList = keyboardSort(listIds);
        return showKeyboard(m, productsByPrice, keyboardList);
    }
    
    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Keyboard> keyboardList = serviceKeyboard.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < keyboardList.size(); i++) {
            listIds.add(keyboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showKeyboard(m, productList, keyboardList);
    }
    
    @GetMapping("/type/{Id}")
    public String getByType(Model m,
            @PathVariable("Id") Integer Id) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Keyboard> keyboardList = serviceKeyboard.findByType(Id);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < keyboardList.size(); i++) {
            listIds.add(keyboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showKeyboard(m, productList, keyboardList);
    }
    
    public List<Keyboard> keyboardSort(List<Integer> listIds) {
        List<Keyboard> concreteKeyboard = new ArrayList();
        for (int i = 0; i < allKeyboards.size(); i++) {
            if (listIds.contains(allKeyboards.get(i).getId())) {
                concreteKeyboard.add(allKeyboards.get(i));
            }
        }
        return concreteKeyboard;
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
        Keyboard keyboard = serviceKeyboard.findById(productId);
        return showOneKeyboardWithInfos(m, product, keyboard);
    }
    
    public String showOneKeyboardWithInfos(Model m, Product product, Keyboard keyboard){
        m.addAttribute("product", product);
        m.addAttribute("keyboardType", keyboard.getType());
        m.addAttribute("keyboardManufacturer", keyboard.getManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if(productsByCategory==null){
            productsByCategory = service.getByCategory(10);
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
        return "keyboardProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductKeyboard(Model m,
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
                return "redirect:/keyboard";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/keyboard";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/keyboard";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/keyboard";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/keyboard";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/keyboard";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/keyboard";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/keyboard";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/keyboard";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/keyboard";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/keyboard";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/keyboard";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/keyboard";
                }
            }
            Category category = categoryService.findById(10); // find category from id
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
                KeyboardManufacturer keyboardManufacturer = serviceKeyboard.findByManufacturerId(manufacturer); // find manufacturer from id
                KeyboardType keyboardType = serviceKeyboard.findByTypeId(type); 
                Keyboard keyboard = new Keyboard(productId, keyboardManufacturer, keyboardType, product); // create product's additional info package(unique for each categoty) 
                boolean keyboardInsert = serviceKeyboard.saveOrUpdate(keyboard); //save this package to db
                if(isUpdate){
                    Keyboard t = serviceKeyboard.findById(productId);
                    index = allKeyboards.indexOf(t);
                    allKeyboards.set(index, keyboard);
                }
                if (keyboardInsert) {                             //*************************************************************************************************** 
                    allKeyboards.add(keyboard);
                    price = null;
                    ammount = null;
                    discount = null;
                    
                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/keyboard";
                } else {
                    String msg = "Something went wrong on inserting keyboard's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/keyboard";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/keyboard";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/keyboard";
        }
    }

    @GetMapping("/update")
    public String updateKeyboardForm(Model m,
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
            productsByCategory = service.getByCategory(10);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/keyboard";
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
        Keyboard keyboard = serviceKeyboard.findById(productId);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(10);
        }
        if (allKeyboards == null) {
            allKeyboards = serviceKeyboard.getAll();
        }
        allKeyboards.remove(keyboard);
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/keyboard";
    }
}
