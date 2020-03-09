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
@RequestMapping("/cpu")
public class CpuController {

    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CpuService serviceCpu;

    List<Cpu> allCpu;
    List<CpuManufacturer> manufacturers;
    List<CpuChip> chips;
    List<Integer> cores;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;

    public String showCpu(Model m, List<Product> productList, List<Cpu> cpuList) {
        m.addAttribute("productList", productList);
        m.addAttribute("cpuList", cpuList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product()); //????????????????????????????????????????
        }
        if (manufacturers == null) {
            manufacturers = serviceCpu.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (chips == null) {
            chips = serviceCpu.findAllChips();
        }
        m.addAttribute("chips", chips);
        if (cores == null) {
            cores = serviceCpu.findAllCores();
        }
        m.addAttribute("cores", cores);
        return "cpu";
    }

    @GetMapping
    public String displayAllCpu(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(3);
        }
        if (allCpu == null) {
            allCpu = serviceCpu.getAll();
        }
        return showCpu(m, productsByCategory, allCpu);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 3);
        List<Integer> listIds = ProductIdByPrice(productsByPrice);
        List<Cpu> cpuList = cpuSort(listIds);
        return showCpu(m, productsByPrice, cpuList);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Cpu> cpuList = serviceCpu.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < cpuList.size(); i++) {
            listIds.add(cpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showCpu(m, productList, cpuList);
    }

    @GetMapping("/chip/{chipId}")
    public String getByChip(Model m,
            @PathVariable("chipId") Integer chipId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Cpu> cpuList = serviceCpu.findByChip(chipId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < cpuList.size(); i++) {
            listIds.add(cpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showCpu(m, productList, cpuList);
    }

    @GetMapping("/cores/{coresId}")
    public String getByCores(Model m,
            @PathVariable("coresId") Integer coresId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Cpu> cpuList = serviceCpu.findByCores(coresId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < cpuList.size(); i++) {
            listIds.add(cpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showCpu(m, productList, cpuList);
    }

    public List<Cpu> cpuSort(List<Integer> listIds) {
        List<Cpu> concreteCpu = new ArrayList();
        for (int i = 0; i < allCpu.size(); i++) {
            if (listIds.contains(allCpu.get(i).getId())) {
                concreteCpu.add(allCpu.get(i));
            }
        }
        return concreteCpu;
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
        Cpu cpu = serviceCpu.findById(productId);
        return showOneCpuWithInfos(m, product, cpu);
    }

    public String showOneCpuWithInfos(Model m, Product product, Cpu cpu) {
        m.addAttribute("product", product);
        m.addAttribute("cpuChip", cpu.getCpuChip());
        m.addAttribute("cpuCores", cpu.getCoresNumber());
        m.addAttribute("cpuManufacturer", cpu.getCpuManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(3);
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
        return "cpuProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductCpu(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("chip") Integer chip,
            @RequestParam("cores") Integer cores,
            @RequestParam("pricee") String price,
            @RequestParam("ammountt") String ammount,
            @RequestParam("discountt") String discount,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("pricee", price);
        redirectAttrs.addFlashAttribute("ammountt", ammount);
        redirectAttrs.addFlashAttribute("discountt", discount);
        redirectAttrs.addFlashAttribute("product", product); // ksanadinoume sto model to simplirwmeno pleon product etsi wste an petaksei kapoio error na ksanagirisei stin forma exontas ta stoixeia tou product simplirwmena
        System.out.println("afroo" + product.getId());
        boolean isUpdate = false;
        if (product.getId() != null) {
            isUpdate = true;
        }
        if (isUpdate) { // katalavainei an exei patithei to update
            if (service.hasSameNameExceptThisOne(product)) {
                String msg = "There is another product with this name";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/cpu";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/cpu";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/cpu";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/cpu";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/cpu";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/cpu";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/cpu";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/cpu";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/cpu";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/cpu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/cpu";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/cpu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/cpu";
                }
            }
            Category category = categoryService.findById(3); // find category from id
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
                CpuManufacturer cpuManufacturer = serviceCpu.findByManufacturerId(manufacturer); // find manufacturer from id
                CpuChip cpuChip = serviceCpu.findByChipId(chip);
                Cpu cpu = new Cpu(productId, cores, cpuChip, cpuManufacturer, product);
                boolean cpuInsert = serviceCpu.saveOrUpdate(cpu); //save this package to db
                if(isUpdate){
                    Cpu t = serviceCpu.findById(productId);
                    index = allCpu.indexOf(t);
                    allCpu.set(index, cpu);
                }
                if (cpuInsert) {                             //*************************************************************************************************** 
                    allCpu.add(cpu);
                    price = null;
                    ammount = null;
                    discount = null;

                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/cpu";
                } else {
                    String msg = "Something went wrong on inserting cpu's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/cpu";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/cpu";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/cpu";
        }
    }

    @GetMapping("/update")
    public String updateCpuForm(Model m,
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
            productsByCategory = service.getByCategory(3);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/cpu";
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
        Cpu cpu = serviceCpu.findById(productId);
        if (allCpu == null) {
            allCpu = serviceCpu.getAll();
        }
        allCpu.remove(cpu);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(3);
        }
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/cpu";
    }
}
