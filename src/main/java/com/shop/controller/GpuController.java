package com.shop.controller;

import static com.shop.controller.CpuController.productsByCategory;
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
@RequestMapping("/gpu")
public class GpuController {

    @Autowired
    ProductService service;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GpuService serviceGpu;

    List<Gpu> allGpu;
    List<GpuManufacturer> manufacturers;
    List<Integer> memories;
    List<GpuChipset> chipsets;
    static List<Product> productsByCategory;
    Integer indexOfProduct = -1;
    Integer index = -1;

    public String showGpu(Model m, List<Product> productList, List<Gpu> gpuList) {
        m.addAttribute("productList", productList);
        m.addAttribute("gpuList", gpuList);
        Product productCheck = (Product) m.getAttribute("product");
        if (productCheck == null) {
            m.addAttribute("product", new Product()); //????????????????????????????????????????
        }
        if (manufacturers == null) {
            manufacturers = serviceGpu.findAllManufacturers();
        }
        m.addAttribute("manufacturers", manufacturers);
        if (chipsets == null) {
            chipsets = serviceGpu.findAllChipsets();
        }
        m.addAttribute("chipsets", chipsets);
        if (memories == null) {
            memories = serviceGpu.findAllMemories();
        }
        m.addAttribute("memories", memories);
        return "gpu";
    }

    @GetMapping
    public String displayAllGpu(Model m) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(4);
        }
        if (allGpu == null) {
            allGpu = serviceGpu.getAll();
        }
        return showGpu(m, productsByCategory, allGpu);
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
        List<Product> productsByPrice = service.getByPrice(initialPrice, finalPr, 4);
        List<Integer> listIds = ProductIdByPrice(productsByPrice);
        List<Gpu> gpuList = gpuSort(listIds);
        return showGpu(m, productsByPrice, gpuList);
    }

    @GetMapping("/manufacturer/{manufacturerId}")
    public String getByManufacturer(Model m,
            @PathVariable("manufacturerId") Integer manufacturerId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Gpu> gpuList = serviceGpu.findByManufacturer(manufacturerId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < gpuList.size(); i++) {
            listIds.add(gpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showGpu(m, productList, gpuList);
    }

    @GetMapping("/chipset/{chipsetId}")
    public String getByChip(Model m,
            @PathVariable("chipsetId") Integer chipsetId) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Gpu> gpuList = serviceGpu.findByChipset(chipsetId);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < gpuList.size(); i++) {
            listIds.add(gpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showGpu(m, productList, gpuList);
    }

    @GetMapping("/memory/{memoryValue}")
    public String getByMemory(Model m,
            @PathVariable("memoryValue") Integer memoryValue) {
        int countCart = ProductController.getCountCart();
        m.addAttribute("countCart", countCart);
        double finalPrice = ProductController.getFinalPrice();
        m.addAttribute("finalPrice", finalPrice);
        List<Gpu> gpuList = serviceGpu.findByMemory(memoryValue);
        List<Integer> listIds = new ArrayList();
        for (int i = 0; i < gpuList.size(); i++) {
            listIds.add(gpuList.get(i).getId());
        }
        List<Product> productList = productSort(listIds);
        return showGpu(m, productList, gpuList);
    }

    public List<Gpu> gpuSort(List<Integer> listIds) {
        List<Gpu> concreteGpu = new ArrayList();
        for (int i = 0; i < allGpu.size(); i++) {
            if (listIds.contains(allGpu.get(i).getId())) {
                concreteGpu.add(allGpu.get(i));
            }
        }
        return concreteGpu;
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
        Gpu gpu = serviceGpu.findById(productId);
        System.out.println("see more *********************_ " + gpu.getGpuChipset() + ", " + gpu.getMemory());
        return showOneGpuWithInfos(m, product, gpu);
    }

    public String showOneGpuWithInfos(Model m, Product product, Gpu gpu) {
        m.addAttribute("gpuP", gpu); //*****************************
        m.addAttribute("product", product);
        m.addAttribute("gpuMemory", gpu.getMemory());
        m.addAttribute("gpuChipset", gpu.getGpuChipset());
        m.addAttribute("gpuManufacturer", gpu.getGpuManufacturer());
        List<Product> listOfProducts = new ArrayList();
        Random random = new Random();
        int index = -1;
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(4);
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
        return "gpuProduct";
    }

    //_______________________________CREATE OR UPDATE___________________________________________
    @PostMapping("/add")
    public String addProductGpu(Model m,
            @ModelAttribute("product") Product product,
            @RequestParam("manufacturer") Integer manufacturer,
            @RequestParam("memory") Integer memory,
            @RequestParam("chipset") Integer chipset,
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
                return "redirect:/gpu";
            }
            if (service.hasSameCodeExceptThisOne(product)) {
                String msg = "There is another product code with this value";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/gpu";
            }

        } else {
            if (service.hasSameName(product)) {
                String msg = "product name already exists";
                redirectAttrs.addFlashAttribute("msgName", msg);
                return "redirect:/gpu";
            }
            if (service.hasSameCode(product)) {
                String msg = "product code already exists";
                redirectAttrs.addFlashAttribute("msgCode", msg);
                return "redirect:/gpu";
            }
        }
        try {
            BigDecimal priceBigDecimal = new BigDecimal(price);
            product.setPrice(priceBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersPrice", msg);
            return "redirect:/gpu";
        }
        try {
            Integer ammountInt = Integer.parseInt(ammount);
            product.setQuantity(ammountInt);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersAmmount", msg);
            return "redirect:/gpu";
        }
        try {
            BigDecimal discounteBigDecimal = new BigDecimal(discount);
            product.setSales(discounteBigDecimal);
        } catch (Exception e) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbersDiscount", msg);
            return "redirect:/gpu";
        }
        if (product.getQuantity() != (int) product.getQuantity()) {
            String msg = "Only numbers allowed";
            redirectAttrs.addFlashAttribute("msgNumbers", msg);
            return "redirect:/gpu";
        }
        try {
            if (!isUpdate) {
                if (file.getContentType().equals("application/octet-stream")) {
                    String msg = "You need to select a photo";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/gpu";
                } else if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/gpu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/gpu";
                }

            } else if (isUpdate && !file.getContentType().equals("application/octet-stream")) {
                if ((!file.getContentType().startsWith("image"))) {
                    String msg = "Wrong File Type";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/gpu";
                } else if (file.getSize() > 4000000) {
                    String msg = "Size must be less than 4Mb";
                    redirectAttrs.addFlashAttribute("msgImg", msg);
                    return "redirect:/gpu";
                }
            }
            Category category = categoryService.findById(4); // find category from id
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
                GpuManufacturer gpuManufacturer = serviceGpu.findByManufacturerId(manufacturer); // find manufacturer from id
                GpuChipset gpuChipset = serviceGpu.findByChipsetId(chipset);
                Gpu gpu = new Gpu(productId, memory, gpuChipset, gpuManufacturer, product);
                boolean gpuInsert = serviceGpu.saveOrUpdate(gpu); //save this package to db
                if(isUpdate){
                    Gpu t = serviceGpu.findById(productId);
                    index = allGpu.indexOf(t);
                    allGpu.set(index, gpu);
                }
                if (gpuInsert) {                             //*************************************************************************************************** 
                    allGpu.add(gpu);
                    price = null;
                    ammount = null;
                    discount = null;

                    redirectAttrs.addFlashAttribute("pricee", price);
                    redirectAttrs.addFlashAttribute("ammountt", ammount);
                    redirectAttrs.addFlashAttribute("discountt", discount);
                    redirectAttrs.addFlashAttribute("product", new Product());
                    String msg = "Product created successfully!";
                    redirectAttrs.addFlashAttribute("msgSuccess", msg);
                    return "redirect:/gpu";
                } else {
                    String msg = "Something went wrong on inserting gpu's info. Please try again";
                    redirectAttrs.addFlashAttribute("msgGeneral", msg);
                    return "redirect:/gpu";
                }
            } catch (Exception e) {
                String msg = "Something went wrong. Check if all the fields are filled!";
                redirectAttrs.addFlashAttribute("msgGeneral", msg);
                return "redirect:/gpu";
            }

        } catch (Exception e) {
            String msg = "Something went wrong. Try again!";
            redirectAttrs.addFlashAttribute("msgGeneral", msg);
            return "redirect:/gpu";
        }
    }

    @GetMapping("/update")
    public String updateGpuForm(Model m,
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
            productsByCategory = service.getByCategory(4);
        }
        indexOfProduct = productsByCategory.indexOf(product);
        return "redirect:/gpu";
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
        Gpu gpu = serviceGpu.findById(productId);
        if (allGpu == null) {
            allGpu = serviceGpu.getAll();
        }
        allGpu.remove(gpu);
        if (productsByCategory == null) {
            productsByCategory = service.getByCategory(4);
        }
        productsByCategory.remove(product);
        String msg = "Product deleted successfully!";
        redirectAttrs.addFlashAttribute("msgSuccess", msg);
        return "redirect:/gpu";
    }
}
