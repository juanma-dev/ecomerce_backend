package com.juanma.ecommerce.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, Long> {
    public Authority findByAname(String aname);
}
