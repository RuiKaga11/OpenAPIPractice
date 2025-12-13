package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// リポジトリ②

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
//	User findByUsername(String username);

	List<User> findAll();

	User save(User user);
		}


