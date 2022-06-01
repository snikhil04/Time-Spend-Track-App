package com.tracker.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tracker.dao.*;
import com.tracker.entities.*;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.request.UserLogin;
import com.tracker.request.UserRegisterRequestDto;
import com.tracker.response.PurchaseHistoryUserResponse;
import com.tracker.response.Response;
import com.tracker.response.UpdatedUserResponse;
import com.tracker.securityconfig.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tracker.request.UserUpdatingDetailsRequestDto;
import com.tracker.response.ProductListUserResponse;
import com.tracker.dto.ProductPurchaseUserRequestDto;

@Service
public class UserService {

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserPurchaseHistoryMongoRepo purchasehistoryrepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserActivityRepo userActivityRepo;

    @Autowired
    private UserWalletRequestsRepo userWalletRequestsRepo;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private JwtUtil jwtutil;

    //REGISTERING A USER
    public User RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {

        if (this.userrepo.findByEmail(userdto.getEmail()) == null) {
            User user = new User();
            if (userdto != null) {
                user.setName(userdto.getName());
                user.setEmail(userdto.getEmail());
                user.setPassword(passwordEncoder.encode(userdto.getPassword()));
                user.setRole(new Role("END_USER"));
                user.setUserWallet(new UserWallet(this.userrepo.getDefaultWalletAmount()));
                user.setActive(true);
                this.userrepo.save(user);

                //SETTING THE ACTIVITY OF THE USER

                UserActivity activity = new UserActivity();
                activity.setUserId(user.getId());
                activity.setUserEmail(user.getEmail());
                activity.setLoginTime(LocalDateTime.now());
                activity.setActivity("REGISTER");
                this.userActivityRepo.save(activity);
                return user;
            } else {
                throw new ValidationException(400, "Bad Request");
            }

        } else {
            throw new ValidationException(400, "User Already Registered");
        }
    }

    //AUTHENTICATING AND LOGIN A USER
    public String UserLogin(UserLogin userLogin) {
        try {
            authenticationmanager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword())
            );
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("Invalid UserName & Password");
        }

        User user = this.userrepo.findByEmail(userLogin.getEmail());

        if (user.isActive() == false) {
            throw new ValidationException(202, "You Are Blocked By System Admin!");
        }
        UserActivity activity = new UserActivity();
        activity.setUserId(user.getId());
        activity.setUserEmail(user.getEmail());
        activity.setLoginTime(LocalDateTime.now());
        activity.setActivity("LOGIN");
        this.userActivityRepo.save(activity);

        return jwtutil.generateToken(userLogin.getEmail(), user);
    }

    // LOGGING OUT USER
    public String Logout() {
        User user = this.userrepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        UserActivity activity = new UserActivity();
        activity.setUserId(user.getId());
        activity.setUserEmail(user.getEmail());
        activity.setLoginTime(LocalDateTime.now());
        activity.setActivity("LOGOUT");
        this.userActivityRepo.save(activity);
        return "Logged Out";
    }

    //UPADTING USER DETAILS
    public UpdatedUserResponse updateUser(UserUpdatingDetailsRequestDto userdto, Principal principal) {

        User user = this.userrepo.findByEmail(principal.getName());
        UpdatedUserResponse response = new UpdatedUserResponse();

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
            }
            user.setRole(user.getRole());
            user.setActive(user.isActive());
            user.getUserWallet().setCurrency(user.getUserWallet().getCurrency());

            response.setName(user.getName());
            response.setEmail(user.getEmail());
            this.userrepo.save(user);
        }

        return response;
    }

    // GETTING USER WALLET BALANCE
    public String UserWallet(Principal principal) {
        User user = this.userrepo.findByEmail(principal.getName());
        return "Your Wallet Balance is : " + user.getUserWallet().getCurrency();
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    public List<ProductListUserResponse> getProductsByCategory(String category) {

        if (!(this.productRepo.CheckProductExist(category).isEmpty())) {

            List<String> productId = this.productRepo.getProductIdByCategory(category);
            List<ProductListUserResponse> productList = new ArrayList<>();

            if (productId.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    ProductListUserResponse jsonresponse = new ProductListUserResponse();
                    Product p = this.productRepo.getProductByProductCategoryId(productId.get(i));
                    jsonresponse.setId(p.getId());
                    jsonresponse.setName(p.getName());
                    jsonresponse.setPrice(p.getPrice());
                    jsonresponse.setDescription(p.getDescription());
                    productList.add(jsonresponse);
                }
            } else {
                for (int i = 0; i < productId.size(); i++) {
                    ProductListUserResponse jsonresponse = new ProductListUserResponse();
                    Product p = this.productRepo.getProductByProductCategoryId(productId.get(i));
                    jsonresponse.setId(p.getId());
                    jsonresponse.setName(p.getName());
                    jsonresponse.setPrice(p.getPrice());
                    jsonresponse.setDescription(p.getDescription());
                    productList.add(jsonresponse);
                }
            }
            return productList;

        } else {
            throw new ValidationException(400, "Product Not Found");
        }
    }

    // PURCHASING PRODUCT
    public PurchaseHistoryUserResponse PurchaseProduct(Principal principal, ProductPurchaseUserRequestDto userPurchaseRequest) {
        User user = this.userrepo.findByEmail(principal.getName());
        Optional<Product> product = this.productRepo.findById(userPurchaseRequest.getProductid());
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        PurchaseHistoryUserResponse response = null;

        if (product.get().getQuantity() > userPurchaseRequest.getProductquantity()) {

            if (user.getUserWallet().getCurrency() >= product.get().getPrice() * userPurchaseRequest.getProductquantity()) {

                purchaseHistory.setProductId(product.get().getId());
                purchaseHistory.setProductName(product.get().getName());
                purchaseHistory.setUserId(user.getId());
                purchaseHistory.setPurchaseDate(LocalDateTime.now());
                purchaseHistory.setProductPrice(product.get().getPrice());
                purchaseHistory.setQuantity(userPurchaseRequest.getProductquantity());
                purchaseHistory.setTotalPrice(product.get().getPrice() * userPurchaseRequest.getProductquantity());

                response = new PurchaseHistoryUserResponse(product.get().getName(), product.get().getPrice(), userPurchaseRequest.getProductquantity(), product.get().getPrice() * userPurchaseRequest.getProductquantity(), LocalDateTime.now());

                // HERE WE ARE UPDATING THE PRODUCT QUANTITY AFTER PURCHASING THE PRODUCT
                product.get().setQuantity(product.get().getQuantity() - userPurchaseRequest.getProductquantity());

                // HERE WE ARE UPDATING THE USER WALLET AMOUNT AFTER PURCHASINGH THE PRODUCT
                user.getUserWallet().setCurrency(
                        user.getUserWallet().getCurrency() - product.get().getPrice() * userPurchaseRequest.getProductquantity());

                this.purchasehistoryrepo.save(purchaseHistory);
                this.productRepo.save(product.get());
                this.userrepo.save(user);
            } else {
                throw new ValidationException(404, "Request to your admin to increase wallet limit " + "Click here and enter your amount " + "redirect:/user/request/");
            }
        } else {
            throw new ValidationException(404, ("Sorry We have only [" + product.get().getQuantity() + "] Quantity "));
        }

        return response;
    }

    // SHOWING PURCHASE HISTORY
    public List<PurchaseHistoryUserResponse> PurchaseHistory(Principal principal) {
        User user = this.userrepo.findByEmail(principal.getName());
        List<PurchaseHistory> purchaseHistoryList = this.purchasehistoryrepo.findByUserId(user.getId());
        List<PurchaseHistoryUserResponse> purchaseHistoryUserResponseList = new ArrayList<>();

        for (PurchaseHistory p : purchaseHistoryList) {
            PurchaseHistoryUserResponse purchaseHistoryUserResponse = new PurchaseHistoryUserResponse();
            purchaseHistoryUserResponse.setProductName(p.getProductName());
            purchaseHistoryUserResponse.setQuantity(p.getQuantity());
            purchaseHistoryUserResponse.setPrice(p.getProductPrice());
            purchaseHistoryUserResponse.setTotalPrice(p.getProductPrice() * p.getQuantity());
            purchaseHistoryUserResponse.setPurchaseDate(p.getPurchaseDate());
            purchaseHistoryUserResponseList.add(purchaseHistoryUserResponse);
        }

        if (purchaseHistoryUserResponseList.size() > 0) {
            return purchaseHistoryUserResponseList;
        } else {
            throw new ValidationException(400, "Sorry! You Did Not Purchased Any Product Yet!");
        }
    }

    //REQUESTING ADMIN TO INCREASE WALLET LIMIT
    public String RequestAdminForWallet(Principal principal, long amount) {

        if (this.userWalletRequestsRepo.getRequestIdByUserEmail(principal.getName()) == null) {
            User user = this.userrepo.findByEmail(principal.getName());
            if (user.getUserWallet().getCurrency() <= 10000) {
                UserRequests requests = new UserRequests();
                requests.setUserEmail(user.getEmail());
                requests.setRequestedAmount(amount);
                requests.setUserCurrentBalance(user.getUserWallet().getCurrency());
                this.userWalletRequestsRepo.save(requests);
                return "Request Successfully Sent For => " + amount;
            } else {
                throw new ValidationException(400, "Request Is Proceed Only When Your Wallet Balance is Less Than 10,000");
            }
        } else {
            return "Your Request For {" + amount + " Rs.} Has Already Sent To The Admin";
        }
    }
}