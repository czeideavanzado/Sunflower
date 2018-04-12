package org.hamster.sunflower_v2.controllers;

import org.hamster.sunflower_v2.domain.models.*;
import org.hamster.sunflower_v2.services.OrderService;
import org.hamster.sunflower_v2.services.ProductService;
import org.hamster.sunflower_v2.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/28/2018
 */
@Controller
@RequestMapping(value = "product")
public class ProductController {

    private ProductService productService;
    private StorageService storageService;
    private OrderService orderService;

    private static String PRODUCT_PATH = "product/";

    @Autowired
    public ProductController(OrderService orderdetailService, ProductService productService, StorageService storageService) {
        this.productService = productService;
        this.storageService = storageService;
        this.orderService = orderdetailService;
    }

    @GetMapping(value = "/sell")
    public String sellProductForm(ModelMap modelMap) {
        ProductDTO product = new ProductDTO();
        User loggedUser = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", loggedUser);
        modelMap.put("product", product);
        return PRODUCT_PATH + "sell";
    }

    @PostMapping(value = "/sell")
    public ModelAndView sellProduct(@ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult result,
                                    WebRequest request, Errors errors, @RequestParam("file") MultipartFile file) {
        if(!result.hasErrors()) {
            if (!file.isEmpty()) {
                productDTO.setPhoto(storageService.storeProduct(file));
            }

            productService.sellProduct(productDTO);
            return new ModelAndView("redirect:/product/sellConfirmation");
        } else {
            return new ModelAndView(PRODUCT_PATH + "sell", "product", productDTO);
        }
    }

    @GetMapping(value = "/sellConfirmation")
    public String sellConfirmed() {
        return PRODUCT_PATH + "sellConfirmation";
    }

    @GetMapping(value = "/view/{id}")
    public String viewProductForm(@PathVariable("id") Long id, ModelMap modelMap) {
        User loggedUser = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("product", productService.find(id));
        return PRODUCT_PATH + "view";
    }

    @GetMapping(value = "/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, ModelMap modelMap) {
        User loggedUser = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (loggedUser != (productService.find(id).getSeller())) {
            return "redirect:/error/403";
        }

        modelMap.put("loggedUser", loggedUser);
        modelMap.put("product", productService.find(id));
        return PRODUCT_PATH + "edit";
    }

    @PostMapping(value = "/edit/{id}")
    public ModelAndView editProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid Product product, BindingResult result,
                                    WebRequest request, Errors errors) {
        if(!result.hasErrors()) {
            productService.updateProduct(product, id);
            return new ModelAndView(PRODUCT_PATH + "editConfirmation", "product", product);
        } else {
            return new ModelAndView(PRODUCT_PATH + "edit", "product", product);
        }
    }

    @GetMapping(value = "/remove/{id}")
    public ModelAndView removeProduct(@PathVariable("id") Long id) {
        User loggedUser = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (loggedUser != (productService.find(id).getSeller())) {
            return new ModelAndView("redirect:/error/403");
        }

        productService.removeProduct(id);
        return new ModelAndView(PRODUCT_PATH + "removeConfirmation", "", "");
    }

    @GetMapping(value = "/myProducts")
    public String myProducts(ModelMap modelMap) {
        User seller = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", seller);
        modelMap.put("products", seller.getProducts());
        return PRODUCT_PATH + "my_products";
    }

    @GetMapping(value = "/myOrders")
    public String myOrders(ModelMap modelMap) {
        User buyer = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        modelMap.put("loggedUser", buyer);
        modelMap.put("orders", buyer.getOrders());
        modelMap.put("details", orderService.findAllDetails());
        return PRODUCT_PATH + "my_orders";
    }

    @PostMapping(value = "/cancel/{id}")
    public ModelAndView cancelTransaction(@PathVariable("id") Long id, @ModelAttribute("transaction") @Valid Transaction transaction, BindingResult result,
                                          WebRequest request, Errors errors) {
        User loggedUser = productService.findByUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (loggedUser != (productService.find(id).getSeller())) {
            return new ModelAndView("redirect:/error/403");
        }


        if(!result.hasErrors()) {
            orderService.cancelTransaction(id);
            List<OrderDetail> details = orderService.findAllDetails();
            for(OrderDetail detail:details){
                if(detail.getOrder().getId() == id)
                    productService.setOpen(detail.getProduct().getId());
            }
            return new ModelAndView("redirect:/product/myOrders");
        } else {
            return new ModelAndView("redirect:/product/myOrders");
        }
    }
}
