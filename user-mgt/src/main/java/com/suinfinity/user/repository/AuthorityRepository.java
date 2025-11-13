package com.suinfinity.user.repository;

import com.suinfinity.user.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {}
