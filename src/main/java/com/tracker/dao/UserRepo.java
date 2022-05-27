package com.tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tracker.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	@Query(value = "select currency from admin a LIMIT 1", nativeQuery = true)
	long getDefaultWalletAmount();

	@Query(value = "select * from user where email =?", nativeQuery = true)
	User findByEmail(String email);

	@Query(value = "select * from user where id =?", nativeQuery = true)
	User findUserByUserId(String id);

}
