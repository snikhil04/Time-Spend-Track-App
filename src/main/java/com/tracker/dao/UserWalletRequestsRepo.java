package com.tracker.dao;

import com.tracker.entities.UserActivity;
import com.tracker.entities.UserRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRequestsRepo extends JpaRepository<UserRequests, Integer> {

    @Query(value = "select * from user_requests where user_email=?", nativeQuery = true)
    UserRequests getRequestIdByUserEmail(String email);

}
