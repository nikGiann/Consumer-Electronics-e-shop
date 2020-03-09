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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/psu")
public class PsuController {

    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PsuService servicePsu;

    List<Psu> allPsu;
    List<PsuManufacturer> manufacturers;
    List<PsuCableManagement> cableManagements;
    List<PsuEfficiency> efficiencies;
    List<Integer> watts;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;

    public String showPsu(Model m, List<Product> productList, List<Psu> psuList) {
        m.addAttribute("productList", productList);
        m.addAttribute("psuList", psuList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product()); //????????????????????????????????????????
        }
        if (manufacturers == null) {
            manufacturers = servicePsu.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (cableManagements == null) {
            cableManagements = servicePsu.findAllCableManagement();
        }
        m.addAttribute("cableManagements", cableManagements);
        if (efficiencies == null) {
            efficiencies = servicePsu.findAllEfficiencies();
        }
        m.addAttribute("efficiencies", efficiencies);
        if (watts == null) {
            watts = servicePsu.findAllWatts();
        }
        m.addAttribute("watts", watts);
        return "psu";
    }

    @GetMapping
    public String displayAllPsu(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(7);
        }
        if (allPsu == null) {
            allPsu = servicePsu.getAll();
        }
        return showPsu(m, productsByCategory, allPsu);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 7);
        List<Integer> listIds = ProductIdByPrice(productsByPrice); //ids of the above products 
        List<Psu> psuList = psuSort(listIds);
        return showPsu(m, productsByPrice, psuList);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Psu> psuList = servicePsu.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < psuList.size(); i++) {
            listIds.add(psuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showPsu(m, productList, psuList);
    }

    @GetMapping("/watt/{wattValue}")
    public String getByType(Model m,
            @PathVariable("wattValue") Integer wattValue) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Psu> psuList = servicePsu.findByWatt(wattValue);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < psuList.size(); i++) {
            listIds.add(psuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showPsu(m, productList, psuList);
    }

    @GetMapping("/cableManagement/{Id}")
    public String getByCableManagement(Model m,
            @PathVariable("Id") Integer Id) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Psu> psuList = servicePsu.findByCableManagement(Id);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < psuList.size(); i++) {
            listIds.add(psuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showPsu(m, productList, psuList);
    }

    @GetMapping("/efficiency/{Id}")
    public String getByInches(Model m,
            @PathVariable("Id") Integer Id) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Psu> psuList = servicePsu.findByEfficiency(Id);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < psuList.size(); i++) {
            listIds.add(psuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showPsu(m, productList, psuList);
    }

    public List<Psu> psuSort(List<Integer> listIds) {
        List<Psu> concretePsu = new ArrayList();
        for (int i = 0; i < allPsu.size(); i++) {
            if (listIds.contains(allPsu.get(i).getId())) {
                concretePsu.add(allPsu.get(i));
            }
        }
        return concretePsu;
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
        Psu psu = servicePsu.findById(productId);
        return showOnePsuWithInfos(m, product, psu);
    }

    public String showOnePsuWithInfos(Model m, Product product, Psu psu) {
        m.addAttribute("product", product);
        m.addAttribute("psuPower", psu.getWatt());
        m.addAttribute("psuCableManagement", psu.getCableManagement());
        m.addAttribute("psuEfficiency", psu.getEfficiency());
        m.addAttribute("psuManufacturer", psu.getManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(7);
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
        return "psuProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductPsu(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("watt") Integer watt,
            @RequestParam("cableManagement") Integer cableManagement,
            @RequestParam("efficiency") Integer efficiency,
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
                return "redirect:/psu";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/psu";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/psu";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/psu";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/psu";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/psu";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/psu";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/psu";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/psu";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/psu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/psu";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/psu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/psu";
                }
            }
            Category category = categoryService.findById(7); // find category from id
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
                PsuManufacturer psuManufacturer = servicePsu.findManufacturerById(manufacturer); // find manufacturer from id
                PsuEfficiency psuEfficiency = servicePsu.findEfficiencyById(efficiency); // find manufacturer from id
                PsuCableManagement psuCableManagement = servicePsu.findCableManagementById(cableManagement); // find towertype from id
                Psu psu = new Psu(productId, watt, product, psuCableManagement, psuEfficiency, psuManufacturer);
                boolean psuInsert = servicePsu.saveOrUpdate(psu); //save this package to db
                if(isUpdate){
                    Psu t = servicePsu.findById(productId);
                    index = allPsu.indexOf(t);
                    allPsu.set(index, psu);
                }
                if (psuInsert) {                             //*************************************************************************************************** 
                    allPsu.add(psu);
                    price = null;
                    ammount = null;
                    discount = null;

                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/psu";
                } else {
                    String msg = "Something went wrong on inserting psu's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/psu";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/psu";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/psu";
        }
    }

    @GetMapping("/update")
    public String updatePsuForm(Model m,
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
            productsByCategory = service.getByCategory(7);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/psu";
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
        Psu psu = servicePsu.findById(productId);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(7);
        }
        if (allPsu == null) {
            allPsu = servicePsu.getAll();
        }
        allPsu.remove(psu);
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/psu";
    }
}
