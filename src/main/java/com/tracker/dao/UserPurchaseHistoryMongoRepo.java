package com.tracker.dao;

import com.tracker.entities.PurchaseHistory;
import com.tracker.response.MostSpendUsersResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPurchaseHistoryMongoRepo extends MongoRepository<PurchaseHistory, Integer> {

    public List<PurchaseHistory> findByUserId(String userId);

    @Aggregation(pipeline = {"{'$group' : {'_id' : '$userId',spendmoney : {$sum : '$totalPrice'}}}"})
    public List<MostSpendUsersResponse> findMostSpendUser();

}