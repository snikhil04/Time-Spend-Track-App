package com.tracker.dao;

import com.tracker.entities.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepo extends JpaRepository<UserActivity, Integer> {

}
