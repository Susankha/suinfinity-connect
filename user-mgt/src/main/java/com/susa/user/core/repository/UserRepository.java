package com.susa.user.core.repository;

import com.susa.user.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByName(String name);

  Long deleteByName(String name);
}
