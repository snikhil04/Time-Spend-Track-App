package com.tracker.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tracker.dao.UserActivityRepo;
import com.tracker.entities.*;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.request.UserRegisterRequestDto;
import com.tracker.response.PurchaseHistoryUserResponse;
import com.tracker.response.UpdatedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tracker.dao.ProductRepo;
import com.tracker.dao.UserPurchaseHistoryMongoRepo;
import com.tracker.dao.UserRepo;
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

    //REGISTERING A USER
    public User RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {
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
            return user;
        }
    }

    //UPADTING USER DETAILS
    public UpdatedUserResponse updateUser(UserUpdatingDetailsRequestDto userdto, User user) {

        User validateduser = user;
        UpdatedUserResponse response = new UpdatedUserResponse();

        if (userdto != null) {
            validateduser.setId(user.getId());
            if (userdto.getName() != null) {
                validateduser.setName(userdto.getName());

            } else {
                validateduser.setName(user.getName());
            }

            if (userdto.getEmail() != null) {
                validateduser.setEmail(userdto.getEmail());

            } else {
                validateduser.setEmail(user.getEmail());
            }

            if (userdto.getPassword() != null) {
                validateduser.setPassword(passwordEncoder.encode(userdto.getPassword()));
            } else {
                validateduser.setPassword(user.getPassword());
            }
            validateduser.setRole(user.getRole());
            validateduser.setActive(user.isActive());
            validateduser.getUserWallet().setCurrency(user.getUserWallet().getCurrency());

            response.setName(validateduser.getName());
            response.setEmail(validateduser.getEmail());
            this.userrepo.save(user);
        }

        return response;
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    public List<ProductListUserResponse> getProductsByCategory(String category) {
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
    }

    // PURCHASING PRODUCT
    public PurchaseHistoryUserResponse PurchaseProduct(User user, ProductPurchaseUserRequestDto userPurchaseRequest) throws Exception {

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
                throw new ValidationException(404, "Request to your admin to increase wallet limit" + "Click here and enter your amount " + "redirect:/user/request/");
            }
        } else {
            throw new ValidationException(404, ("Sorry We have only [" + product.get().getQuantity() + "] Quantity "));
        }

        return response;
    }

    // SHOWING PURCHASE HISTORY
    public List<PurchaseHistoryUserResponse> PurchaseHistory(String email) {
        User user = this.userrepo.findByEmail(email);
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
        return purchaseHistoryUserResponseList;
    }

    //REQUESTING ADMIN TO INCREASE WALLET LIMIT
    public UserRequests RequestAdminForWallet(User user, long amount){
        UserRequests requests = new UserRequests();
        requests.setUserEmail(user.getEmail());
        requests.setRequestedAmount(amount);
        requests.setUserCurrentBalance(user.getUserWallet().getCurrency());
        return requests;
    }

}
