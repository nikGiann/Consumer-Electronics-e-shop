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
@RequestMapping("/motherboard")
public class MotherboardController {

    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MotherboardService serviceMother;

    List<Motherboard> allMotherboards;
    List<MotherboardManufacturer> manufacturers;
    List<MotherboardSocket> sockets;
    List<MotherboardSize> sizes;
    List<MotherboardChipset> chipsets;
    List<MotherboardPort> ports;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;

    public String showMotherboard(Model m, List<Product> productList, List<Motherboard> motherboardList) {
        m.addAttribute("productList", productList);
        m.addAttribute("motherboardList", motherboardList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product());
        }
        if (manufacturers == null) {
            manufacturers = serviceMother.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (sockets == null) {
            sockets = serviceMother.findAllSockets();;
        }
        m.addAttribute("sockets", sockets);
        if (sizes == null) {
            sizes = serviceMother.findAllSizes();
        }
        m.addAttribute("sizes", sizes);
        if (chipsets == null) {
            chipsets = serviceMother.findAllChipsets();
        }
        m.addAttribute("chipsets", chipsets);
        if (ports == null) {
            ports = serviceMother.findAllPorts();
        }
        m.addAttribute("ports", ports);
//        m.addAttribute("product", new Product());
        return "motherboard";
    }

    @GetMapping
    public String displayAllMotherboards(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(2);
        }
        if (allMotherboards == null) {
            allMotherboards = serviceMother.getAll();
        }
        return showMotherboard(m, productsByCategory, allMotherboards);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 2);
        List<Integer> listIds = ProductIdByPrice(productsByPrice); //ids of the above products 
        List<Motherboard> motherboardList = motherboardSort(listIds); //motherboards with the above Ids
        return showMotherboard(m, productsByPrice, motherboardList);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Motherboard> motherboardList = serviceMother.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < motherboardList.size(); i++) {
            listIds.add(motherboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMotherboard(m, productList, motherboardList);
    }

    @GetMapping("/socket/{typeId}")
    public String getBySocket(Model m,
            @PathVariable("typeId") Integer typeId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Motherboard> motherboardList = serviceMother.findBySocket(typeId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < motherboardList.size(); i++) {
            listIds.add(motherboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMotherboard(m, productList, motherboardList);
    }

    @GetMapping("/size/{sizeId}")
    public String getBySize(Model m,
            @PathVariable("sizeId") Integer sizeId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Motherboard> motherboardList = serviceMother.findBySize(sizeId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < motherboardList.size(); i++) {
            listIds.add(motherboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMotherboard(m, productList, motherboardList);
    }

    @GetMapping("/chipset/{chipsetId}")
    public String getByChipset(Model m,
            @PathVariable("chipsetId") Integer chipsetId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Motherboard> motherboardList = serviceMother.findByChipset(chipsetId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < motherboardList.size(); i++) {
            listIds.add(motherboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMotherboard(m, productList, motherboardList);
    }

    @GetMapping("/port/{portId}")
    public String getByPort(Model m,
            @PathVariable("portId") Integer portId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Motherboard> motherboardList = serviceMother.findByPort(portId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < motherboardList.size(); i++) {
            listIds.add(motherboardList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showMotherboard(m, productList, motherboardList);
    }

    public List<Motherboard> motherboardSort(List<Integer> listIds) {
        List<Motherboard> concreteMotherboards = new ArrayList();
        for (int i = 0; i < allMotherboards.size(); i++) {
            if (listIds.contains(allMotherboards.get(i).getId())) {
                concreteMotherboards.add(allMotherboards.get(i));
            }
        }
        return concreteMotherboards;
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
        Motherboard motherboard = serviceMother.findById(productId);
        return showOneMotherboardWithInfos(m, product, motherboard);
    }

    public String showOneMotherboardWithInfos(Model m, Product product, Motherboard motherboard) {
        m.addAttribute("product", product);
        m.addAttribute("motherboardChipset", motherboard.getMotherboardChipset());
        m.addAttribute("motherboardSize", motherboard.getMotherboardSize());
        m.addAttribute("motherboardSocket", motherboard.getMotherboardSocket());
        m.addAttribute("motherboardPort", motherboard.getMotherboardPort());
        m.addAttribute("motherboardManufacturer", motherboard.getMotherboardManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(2);
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
        return "motherboardProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductMotherboard(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("socket") Integer socket,
            @RequestParam("size") Integer size,
            @RequestParam("chipset") Integer chipset,
            @RequestParam("ports") Integer ports,
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
                String msg = "There is another Product with this name";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/motherboard";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/motherboard";
            }
        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/motherboard";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/motherboard";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/motherboard";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/motherboard";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/motherboard";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/motherboard";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/motherboard";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/motherboard";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/motherboard";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/motherboard";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/motherboard";
                }
            }
            Category category = categoryService.findById(2); // find category from id
            product.setCategory(category); // add category to product
            System.out.println("Controler*************************  " + product.getId());
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
                MotherboardManufacturer motherManufacturer = serviceMother.findManufacturerById(manufacturer); // find manufacturer from id
                MotherboardChipset motherboardChipset = serviceMother.findChipsetById(chipset);
                MotherboardPort motherboardPort = serviceMother.findPortById(ports);
                MotherboardSize motherboardSize = serviceMother.findSizeById(size);
                MotherboardSocket motherboardSocket = serviceMother.findSocketById(socket);
                Motherboard motherboard = new Motherboard(productId, motherboardChipset, motherManufacturer, motherboardPort, motherboardSize, motherboardSocket, product);
                boolean motherboardInsert = serviceMother.saveOrUpdate(motherboard); //save this package to db
                if(isUpdate){
                    Motherboard t = serviceMother.findById(productId);
                    index = allMotherboards.indexOf(t);
                    allMotherboards.set(index, motherboard);
                }
                if (motherboardInsert) {                             //*************************************************************************************************** 
                    allMotherboards.add(motherboard);
                    price = null;
                    ammount = null;
                    discount = null;

                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/motherboard";
                } else {
                    String msg = "Something went wrong on inserting motherboard's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/motherboard";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/motherboard";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/motherboard";
        }
    }

    @GetMapping("/update")
    public String updateMotherboardForm(Model m,
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
            productsByCategory = service.getByCategory(2);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/motherboard";
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
        Motherboard motherboard = serviceMother.findById(productId);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(2);
        }
        if (allMotherboards == null) {
            allMotherboards = serviceMother.getAll();
        }
        allMotherboards.remove(motherboard);
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/motherboard";
    }

}
