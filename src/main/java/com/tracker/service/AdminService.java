package com.tracker.service;

import java.util.*;

import javax.validation.Valid;

import com.tracker.dao.UserPurchaseHistoryMongoRepo;
import com.tracker.entities.PurchaseHistory;
import com.tracker.response.AllUsersPurchaseHistoryResponse;
import com.tracker.response.MostSpendUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.tracker.dao.ProductRepo;
import com.tracker.dao.UserRepo;
import com.tracker.request.AddProductRequestDto;
import com.tracker.request.AdminUpdatingUserRequestDto;
import com.tracker.entities.Product;
import com.tracker.entities.ProductCategory;
import com.tracker.entities.User;

@Service
public class AdminService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserPurchaseHistoryMongoRepo userpurchasehistorymongorepo;

    // GETTING ALL PRODUCTS
    public List<Product> getAllProducts() {
        List<Product> products = this.productRepo.findAll();
        return products;
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    public List<Product> getProductsByCategory(String category) {
        List<String> productId = this.productRepo.getProductIdByCategory(category);
        List<Product> productList = new ArrayList<>();
        for (String id : productId) {
            Product p = this.productRepo.getProductByProductCategoryId(id);
            productList.add(p);
        }
        return productList;
    }

    // ADDING PRODUCT IN DATABASE
    public Product AddProduct(@Valid @RequestBody AddProductRequestDto addProductRequestDto, String category) {
        Product product = new Product();

        if (addProductRequestDto != null) {
            product.setName(addProductRequestDto.getName());
            product.setPrice(addProductRequestDto.getPrice());
            product.setDescription(addProductRequestDto.getDescription());
            if (addProductRequestDto.getCategory() != null) {
                product.setProductCategory(new ProductCategory(addProductRequestDto.getCategory()));
            } else {
                addProductRequestDto.setCategory(category);
                product.setProductCategory(new ProductCategory(addProductRequestDto.getCategory()));
            }
            product.setQuantity(addProductRequestDto.getQuantity());
            this.productRepo.save(product);
            return product;
        } else {
            return product;
        }
    }

    // UPDATING PRODUCT IN DATABASE
    public Product UpdateProduct(@RequestBody AddProductRequestDto addProductRequestDto, @PathVariable("id") String id) {
        Optional<Product> p = this.productRepo.findById(id);
        if (p.isPresent()) {
            p.get().setId(id);
            if (addProductRequestDto.getName() != null) {
                p.get().setName(addProductRequestDto.getName());
            } else {
                addProductRequestDto.setName(p.get().getName());
                p.get().setName(addProductRequestDto.getName());
            }

            if (addProductRequestDto.getPrice() > 0) {
                p.get().setPrice(addProductRequestDto.getPrice());
            } else {
                addProductRequestDto.setPrice(p.get().getPrice());
                p.get().setPrice(addProductRequestDto.getPrice());
            }
            if (addProductRequestDto.getDescription() != null) {
                p.get().setDescription(addProductRequestDto.getDescription());
            } else {
                addProductRequestDto.setDescription(p.get().getDescription());
                p.get().setDescription(addProductRequestDto.getDescription());
            }
            if (addProductRequestDto.getCategory() != null) {
                p.get().getProductCategory().setCategory(addProductRequestDto.getCategory());
            } else {
                addProductRequestDto.setCategory(p.get().getProductCategory().getCategory());
                p.get().getProductCategory().setCategory(addProductRequestDto.getCategory());
            }
            if (addProductRequestDto.getQuantity() > 0) {
                p.get().setQuantity(addProductRequestDto.getQuantity());
            } else {
                addProductRequestDto.setQuantity(p.get().getQuantity());
                p.get().setQuantity(addProductRequestDto.getQuantity());
            }
            this.productRepo.save(p.get());
            return p.get();
        } else {
            return p.get();
        }
    }

    // DELETING A PARTICULAR PRODUCT
    public Optional<Product> DeleteProduct(String id) {
        Optional<Product> product = this.productRepo.findById(id);
        if (product.isPresent()) {
            this.productRepo.delete(product.get());
            return product;
        } else {
            return product;
        }
    }

    // GETTING THE LIST OF ALL USERS
    public List<User> getAllUsers() {
        List<User> usersList = this.userrepo.findAll();
        return usersList;
    }

    // GETTING A PARTICULAR USER
    public User getParticularUser(String email) {
        User user = this.userrepo.findByEmail(email);
        return user;
    }

    // UPDATING A PARTICULAR USER
    public User UpdateUser(String email, AdminUpdatingUserRequestDto userdto) {
        User user = this.userrepo.findByEmail(email);
        if (userdto != null) {
            user.setId(user.getId());
            if (userdto.getName() != null) {
                user.setName(userdto.getName());
            } else {
                user.setName(user.getName());
            }
            if (userdto.getEmail() != null) {
                user.setEmail(userdto.getEmail());
            } else {
                user.setEmail(user.getEmail());
            }
            if (userdto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userdto.getPassword()));
            } else {
                user.setPassword(user.getPassword());
                System.out.println(user.getPassword());
            }
            if (userdto.getRole() != null) {
                user.getRole().setUserRole(userdto.getRole());
            } else {
                user.getRole().setUserRole(user.getRole().getUserRole());
            }

            user.setActive(userdto.isActive());
//            if (userdto.isActive() == true) {
//
//            } else if (userdto.isActive() == false) {
//                user.setActive(false);
//            } else {
//                user.setActive(user.isActive());
//            }

            if (userdto.getCurrency() > 0) {
                user.getUserWallet().setCurrency(userdto.getCurrency());
            } else {
                user.getUserWallet().setCurrency(user.getUserWallet().getCurrency());
            }
            this.userrepo.save(user);
            return user;
        } else {
            return user;
        }
    }

    // UPDATING A PARTICULAR USER WALLET
    public User UpdateUserWallet(String email, long currency) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            user.getUserWallet().setCurrency(currency);
            this.userrepo.save(user);
            return user;
        } else {
            return user;
        }
    }

    // DISABLING A PARTICULAR USER
    public User DisableParticularUser(String email) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            user.setActive(false);
            this.userrepo.save(user);
            return user;
        } else {
            return user;
        }
    }

    // DELETING A PARTICULAR USER
    public User DeleteUser(String email) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            this.userrepo.delete(user);
            return user;
        } else {
            return user;
        }
    }

    // GETTING ALL USER'S PURCHASE HISTORY
    public List<AllUsersPurchaseHistoryResponse> UsersPurchaseHistory() {
        List<PurchaseHistory> purchaseHistoryList = this.userpurchasehistorymongorepo.findAll();
        List<AllUsersPurchaseHistoryResponse> response = new ArrayList<>();

        for (PurchaseHistory p : purchaseHistoryList) {
            AllUsersPurchaseHistoryResponse userhistory = new AllUsersPurchaseHistoryResponse();
            User user = this.userrepo.findUserByUserId(p.getUserId());
            userhistory.setId(p.getId());
            userhistory.setUserId(p.getUserId());
            userhistory.setUserName(user.getName());
            userhistory.setProductId(p.getProductId());
            userhistory.setProductName(p.getProductName());
            userhistory.setQuantity(p.getQuantity());
            userhistory.setProductPrice(p.getProductPrice());
            userhistory.setTotalprice(p.getTotalPrice());
            userhistory.setPurchasedDate(p.getPurchaseDate());
            response.add(userhistory);
        }
        return response;
    }

    //MOST PURCHASED PRODUCT HISTORY
    public List<MostSpendUsersResponse> MostPurchasedProduct() {

        List<MostSpendUsersResponse> userIdLIst = this.userpurchasehistorymongorepo.findMostSpendUser();
        List<MostSpendUsersResponse> spendUsersResponseList = new ArrayList<>();

        for (MostSpendUsersResponse u : userIdLIst) {
            User user = this.userrepo.findUserByUserId(u.getId());
            MostSpendUsersResponse response = new MostSpendUsersResponse();
            response.setId(u.getId());
            response.setSpendmoney(u.getSpendmoney());
            response.setUserName(user.getName());
            response.setUserEmail(user.getEmail());
            spendUsersResponseList.add(response);
        }
        return spendUsersResponseList;
    }

    public List<PurchaseHistory> UserPurchaseHistory(User user) {
        List<PurchaseHistory> purchaseHistoryList = this.userpurchasehistorymongorepo.findByUserId(user.getId());
        return purchaseHistoryList;
    }
}