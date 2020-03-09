package com.shop.controller;

import com.shop.entities.*;
import com.shop.service.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ram")
public class RamController {

    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    RamService serviceRam;

    List<Ram> allRam;
    List<RamManufacturer> manufacturers;
    List<BigDecimal> voltages;
    List<Integer> frequencies;
    List<RamSize> sizes;
    List<RamType> types;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;

    public String showRam(Model m, List<Product> productList, List<Ram> ramList) {
        m.addAttribute("productList", productList);
        m.addAttribute("ramList", ramList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product());
        }
        if (manufacturers == null) {
            manufacturers = serviceRam.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (voltages == null) {
            voltages = serviceRam.findAllVoltage();
        }
        m.addAttribute("voltages", voltages);
        if (frequencies == null) {
            frequencies = serviceRam.findAllFrequencies();
        }
        m.addAttribute("frequencies", frequencies);
        m.addAttribute("voltages", voltages);
        if (sizes == null) {
            sizes = serviceRam.findAllSizes();
        }
        m.addAttribute("sizes", sizes);
        m.addAttribute("voltages", voltages);
        if (types == null) {
            types = serviceRam.findAllTypes();
        }
        m.addAttribute("types", types);
        return "ram";
    }

    @GetMapping
    public String displayAllRam(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(5);
        }
        if (allRam == null) {
            allRam = serviceRam.getAll();
        }
        return showRam(m, productsByCategory, allRam);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 5);
        List<Integer> listIds = ProductIdByPrice(productsByPrice);
        List<Ram> ramList = ramSort(listIds);
        return showRam(m, productsByPrice, ramList);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Ram> ramList = serviceRam.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < ramList.size(); i++) {
            listIds.add(ramList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showRam(m, productList, ramList);
    }

    @GetMapping("/type/{typeId}")
    public String getByType(Model m,
            @PathVariable("typeId") Integer typeId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Ram> ramList = serviceRam.findByType(typeId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < ramList.size(); i++) {
            listIds.add(ramList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showRam(m, productList, ramList);
    }

    @GetMapping("/size/{sizeId}")
    public String getBySize(Model m,
            @PathVariable("sizeId") Integer sizeId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Ram> ramList = serviceRam.findBySize(sizeId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < ramList.size(); i++) {
            listIds.add(ramList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showRam(m, productList, ramList);
    }

    @GetMapping("/frequency/{frequencyValue}")
    public String getByCores(Model m,
            @PathVariable("frequencyValue") Integer frequencyValue) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Ram> ramList = serviceRam.findByFrequency(frequencyValue);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < ramList.size(); i++) {
            listIds.add(ramList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showRam(m, productList, ramList);
    }

    @GetMapping("/voltage/{voltageValue:.+}")
    public String getByVoltage(Model m,
            @PathVariable("voltageValue") BigDecimal voltageValue) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Ram> ramList = serviceRam.findByVoltage(voltageValue);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < ramList.size(); i++) {
            listIds.add(ramList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showRam(m, productList, ramList);
    }

    public List<Ram> ramSort(List<Integer> listIds) {
        List<Ram> concreteRam = new ArrayList();
        for (int i = 0; i < allRam.size(); i++) {
            if (listIds.contains(allRam.get(i).getId())) {
                concreteRam.add(allRam.get(i));
            }
        }
        return concreteRam;
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
            @PathVariable("Id") Integer productId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        Product product = service.findById(productId);
        Ram ram = serviceRam.findById(productId);
        return showOneRamWithInfos(m, product, ram);
    }

    public String showOneRamWithInfos(Model m, Product product, Ram ram) {
        m.addAttribute("product", product);
        m.addAttribute("ramType", ram.getType());
        m.addAttribute("ramVoltage", ram.getVoltage());
        m.addAttribute("ramFrequency", ram.getFrequency());
        m.addAttribute("ramSize", ram.getSize1());
        m.addAttribute("ramManufacturer", ram.getManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(5);
        }
        int listSize = productsByCategory.size();
        int a;
        if (listSize >= 5) {
            a = 4;          // Theloume 4 proionta
        } else {
            a = listSize - 1;
            /*se periptosi omos pou iparxoun 4 i ligotero proionta,
            afairoume 1 proion (Auto pou fainetai sti selide me infos)*/
        }
        for (int i = 0; i < a; i++) {
            Product p = new Product();
            do {
                index = random.nextInt((productsByCategory.size() - 1) + 1);
                p = productsByCategory.get(index);
            } while (listOfProducts.contains(p) || p.getId() == product.getId());
            listOfProducts.add(p);
        }
        m.addAttribute("listOfProducts", listOfProducts);
        return "ramProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductRam(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("voltage") BigDecimal voltage, //****************************************BigDecimal************************************
            @RequestParam("frequency") Integer frequency,
            @RequestParam("size") Integer size1,
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
                return "redirect:/ram";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/ram";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/ram";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/ram";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/ram";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/ram";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/ram";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/ram";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/ram";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/ram";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/ram";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/ram";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/ram";
                }
            }
            Category category = categoryService.findById(5); // find category from id
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
                RamManufacturer ramManufacturer = serviceRam.findByManufacturerId(manufacturer); // find manufacturer from id
                RamSize ramSize = serviceRam.findBySizeId(size1);
                RamType ramType = serviceRam.findByTypeId(type);
                Ram ram = new Ram(productId, voltage, product, frequency, ramManufacturer, ramSize, ramType);
                boolean ramInsert = serviceRam.saveOrUpdate(ram); //save this package to db
                if(isUpdate){
                    Ram t = serviceRam.findById(productId);
                    index = allRam.indexOf(t);
                    allRam.set(index, ram);
                }
                if (ramInsert) {                             //*************************************************************************************************** 
                    allRam.add(ram);
                    price = null;
                    ammount = null;
                    discount = null;

                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/ram";
                } else {
                    String msg = "Something went wrong on inserting tower's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/ram";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/ram";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/ram";
        }
    }

    @GetMapping("/update")
    public String updatetowerForm(Model m,
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
            productsByCategory = service.getByCategory(5);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/ram";
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
        Ram ram = serviceRam.findById(productId);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(5);
        }
        if (allRam == null) {
            allRam = serviceRam.getAll();
        }
        allRam.remove(ram);
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/ram";
    }
}
