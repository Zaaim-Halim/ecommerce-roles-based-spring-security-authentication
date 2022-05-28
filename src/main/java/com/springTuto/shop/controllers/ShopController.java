package com.springTuto.shop.controllers;

import com.springTuto.account.models.AccountEntity;
import com.springTuto.account.models.AppUser;
import com.springTuto.account.services.AccountService;
import com.springTuto.shop.models.ShoppingCart;
import com.springTuto.shop.servicies.ProductService;
import com.springTuto.shop.servicies.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/User")
public class ShopController {
    private final ShopService shopService;
    private final AccountService accountService;
    private final ProductService productService;
    @Autowired
    public ShopController(ShopService shopService, AccountService accountService, ProductService productService) {
        this.shopService = shopService;
        this.accountService = accountService;
        this.productService = productService;
    }

    @GetMapping("/index")
    public String userIndexView(Model model,
                                @RequestParam(value = "user", required = false,defaultValue = "") String email) {


        String Email = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountEntity user = accountService.getAccountByEmail(Email);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("user", user);
        model.addAttribute("shop", shopService.getShoppingCartByUser((AppUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "index";
    }
    @GetMapping("/cart")
    public String cartView(Model model, @RequestParam("id") Optional<Long> id, Authentication auth){
        if(id.isPresent()){
            model.addAttribute("shop", shopService.getShoppingCartById(id.get()));
        }else{
            model.addAttribute("shop", shopService.creatEmptyCart((AppUser)auth.getPrincipal()));
        }

        model.addAttribute("user", accountService.getAccountByEmail(auth.getName()));
        return "cart";
    }
    @PostMapping("/add-cart")
    public String addToCart(Model model, Authentication auth, @RequestParam("id") Long id,
                            @RequestParam("quantity") Integer quantity ) {
        shopService.addItem((AppUser) auth.getPrincipal(),id,quantity);
        return "redirect:/User/index";
    }
    @GetMapping("/remove-cartItem/{id}")
    public String addToCart(Model model, Authentication auth, @PathVariable("id") Long id) {
        ShoppingCart shop = shopService.removeItem((AppUser) auth.getPrincipal(),id);
        model.addAttribute("shop", shop);
        model.addAttribute("user", accountService.getAccountByEmail(((AppUser) ((AppUser) auth.getPrincipal())).getUsername()));
        return "cart";
    }

    @GetMapping("/update-cartItem")
    public String updateItemQuantityCart(Model model, Authentication auth, @RequestParam("id") Long id,
                            @RequestParam("quantity") Integer quantity ) {
        ShoppingCart shop = shopService.updateItemQuantity((AppUser) auth.getPrincipal(),id,quantity);
        model.addAttribute("shop", shop);
        System.out.println( ((AppUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
        model.addAttribute("user", accountService.getAccountByEmail(((AppUser) ((AppUser) auth.getPrincipal())).getUsername()));
        return "cart";
    }




}
