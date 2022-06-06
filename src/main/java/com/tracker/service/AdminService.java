package com.tracker.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.tracker.dao.*;
import com.tracker.dto.UserWalletDtoJson;
import com.tracker.entities.*;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.exceptionhandler.generic.ResponseDTO;
import com.tracker.exceptionhandler.generic.ValidationErrorResponse;
import com.tracker.response.AllUsersPurchaseHistoryResponse;
import com.tracker.response.ApiResponseDTO;
import com.tracker.response.MostSpendUsersResponse;
import com.tracker.response.Response;
import com.tracker.response.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.tracker.request.AddProductRequestDto;
import com.tracker.request.AdminUpdatingUserRequestDto;

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

    @Autowired
    private UserActivityRepo userActivityRepo;

    @Autowired
    UserWalletRequestsRepo userWalletRequestsRepo;

    //ADMIN LOGOUT
    public String AdminLogout() {
        User user = this.userrepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        UserActivity activity = new UserActivity();
        activity.setUserId(user.getId());
        activity.setUserEmail(user.getEmail());
        activity.setLoginTime(LocalDateTime.now());
        activity.setActivity("LOGOUT");
        this.userActivityRepo.save(activity);
        return "Logged Out";
    }

    // GETTING ALL PRODUCTS
    public List<Product> getAllProducts() {
        List<Product> products = this.productRepo.findAll();
        if (products.size() > 0) {
            return products;
        } else {
            throw new ValidationException(400, "Products Not Found");
        }
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    public List<Product> getProductsByCategory(String category) {
        List<String> productId = this.productRepo.getProductIdByCategory(category);
        if (productId.size() > 0) {
            List<Product> productList = new ArrayList<>();
            for (String id : productId) {
                Product p = this.productRepo.getProductByProductCategoryId(id);
                productList.add(p);
            }
            return productList;
        } else {
            throw new ValidationException(400, "No Products Are Available For This Product Category");
        }
    }

    // ADDING PRODUCT IN DATABASE
    public Product AddProduct(@Valid AddProductRequestDto addProductRequestDto) {
        Product product = new Product();

        if (addProductRequestDto != null) {
            product.setName(addProductRequestDto.getName());
            product.setPrice(addProductRequestDto.getPrice());
            product.setDescription(addProductRequestDto.getDescription());
            product.setProductCategory(new ProductCategory(addProductRequestDto.getCategory()));
            product.setQuantity(addProductRequestDto.getQuantity());
            this.productRepo.save(product);
            return product;
        } else {
            throw new ValidationException(400, "Bad Request");
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
            throw new ValidationException(400, "Product Not Found");
        }
    }

    // DELETING A PARTICULAR PRODUCT
    public Optional<Product> DeleteProduct(String id) {
        Optional<Product> product = this.productRepo.findById(id);
        if (product.isPresent()) {
            this.productRepo.delete(product.get());
            return product;
        } else {
            throw new ValidationException(404, "Product Not Found");
        }
    }

    // GETTING THE LIST OF ALL USERS
    public List<User> getAllUsers() {
        List<User> usersList = this.userrepo.findAll();
        List<User> list = new ArrayList<>();
        if (usersList.size() > 0) {
            usersList.forEach(user -> {
                if (user.getRole().getUserRole().equals("END_USER")) {
                    list.add(user);
                }
            });
            return list;
        } else {
            throw new ValidationException(404, "Users Not Found");
        }
    }

    // GETTING A PARTICULAR USER
    public User getParticularUser(String email) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }

    // UPDATING A PARTICULAR USER
    public User UpdateUser(String email, AdminUpdatingUserRequestDto userdto) {
        User user = this.userrepo.findByEmail(email);

        if (user != null) {
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

                if (userdto.getCurrency() > 0) {
                    user.getUserWallet().setCurrency(userdto.getCurrency());
                } else {
                    user.getUserWallet().setCurrency(user.getUserWallet().getCurrency());
                }
                this.userrepo.save(user);
                return user;
            } else {
                throw new ValidationException(404, "Bad Request");
            }
        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }

    // UPDATING A PARTICULAR USER WALLET
    public String UpdateUserWallet(String email, UserWalletDtoJson userWalletDtoJson) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            if (userWalletDtoJson.getCurrency() <= 0) {
                throw new ValidationException(400, "Wallet Can't Be Updated At 0 Rs");
            } else {
                user.getUserWallet().setCurrency(userWalletDtoJson.getCurrency());
                this.userrepo.save(user);
                return "User Wallet Updated =>" + userWalletDtoJson.getCurrency();
            }
        } else {
            throw new ValidationException(404, "User Not Found");
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
            throw new ValidationException(404, "User Not Found");
        }
    }

    // DELETING A PARTICULAR USER
    public User DeleteUser(String email) {
        User user = this.userrepo.findByEmail(email);
        List<PurchaseHistory> purchaseHistoryList = this.userpurchasehistorymongorepo.findByUserId(user.getId());
        UserRequests request = this.userWalletRequestsRepo.getRequestIdByUserEmail(email);
        if (user != null) {

            //DELETING USER PURCHASE HISTORY IF EXISTS
            if (!(purchaseHistoryList.isEmpty())){
                this.userpurchasehistorymongorepo.deleteAll(purchaseHistoryList);
            }

            //DELETING WALLLET REQUEST IF EXISTS
            if (request!=null){
                this.userWalletRequestsRepo.delete(request);
            }
            this.userrepo.delete(user);
            return user;
        } else {
            throw new ValidationException(404, "User Not Found");
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

        //IT CONTAINS ALL ID'S OF TOP SPEND USERS
        List<MostSpendUsersResponse> userIdLIst = this.userpurchasehistorymongorepo.findMostSpendUser();

        //IT CONTAINS ALL THE INFORMATION OF TOP SPEND USERS
        List<MostSpendUsersResponse> spendUsersResponseList = new ArrayList<>();

        // IT IS FILTERED LIST OF TOP SPEND USER [TOP TO BOTTOM {5000,4000,3000,2000} LIKE THIS]
        List<MostSpendUsersResponse> topSpendUsersList = new ArrayList<>();

        for (MostSpendUsersResponse u : userIdLIst) {
            User user = this.userrepo.findUserByUserId(u.getId());
            MostSpendUsersResponse response = new MostSpendUsersResponse();
            response.setId(u.getId());
            response.setSpendmoney(u.getSpendmoney());
            response.setUserName(user.getName());
            response.setUserEmail(user.getEmail());
            spendUsersResponseList.add(response);
        }

        //IT FILTERS THE TOP SPEND USERS FROM [TOP TO BOTTOM {5000,4000,3000,2000} LIKE THIS]
        topSpendUsersList = spendUsersResponseList.stream().sorted(Comparator.comparingLong(MostSpendUsersResponse::getSpendmoney).reversed()).collect(Collectors.toList());

        //HERE WE ARE ASSIGNNG THE VALUE OF FILTERED RESULT TO [TOPSPENDUSERSLIST => LIST]
        spendUsersResponseList = topSpendUsersList;

        if (spendUsersResponseList.size() > 0) {
            return spendUsersResponseList;
        } else {
            throw new ValidationException(404, "Top Spend Users List Is Empty");
        }

    }

    //GETTING A PARTICULAR USER PURCHASE HISTORY
    public List<PurchaseHistory> UserPurchaseHistory(String email) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            List<PurchaseHistory> purchaseHistoryList = this.userpurchasehistorymongorepo.findByUserId(user.getId());
            if (purchaseHistoryList.size() > 0) {
                return purchaseHistoryList;
            } else {
                throw new ValidationException(404, "User Has Not Purchased Any Product");
            }
        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }

    //RECEIVING ALL THE WALLET REQUESTS FROM USERS
    public List<UserRequests> WalletRequests() {
        List<UserRequests> userRequestsList = this.userWalletRequestsRepo.findAll();
        if (userRequestsList.size() > 0) {
            return userRequestsList;
        } else {
            throw new ValidationException(404, "No More Requests Here");
        }
    }

    //UPDATING USER WALLET AS PER USER REQUEST
    public String UpdateUserWalletOnRequest(String email, UserWalletDtoJson userWalletDtoJson) {
        UserRequests request = this.userWalletRequestsRepo.getRequestIdByUserEmail(email);
        if (request != null) {
            if (userWalletDtoJson.getCurrency() == request.getRequestedAmount()) {
                this.userWalletRequestsRepo.delete(request);
                return UpdateUserWallet(email, userWalletDtoJson);
            } else {
                throw new ValidationException(400, "Only Requested Amount=> {" + request.getRequestedAmount() + "} is Accepted");
            }
        } else {
            throw new ValidationException(400, "No Request Found For This User");
        }
    }
}

