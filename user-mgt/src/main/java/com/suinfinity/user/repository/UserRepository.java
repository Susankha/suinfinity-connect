package com.suinfinity.user.repository;

import com.suinfinity.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByName(String name);

  Long deleteByName(String name);
}
