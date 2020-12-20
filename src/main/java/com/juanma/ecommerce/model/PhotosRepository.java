package com.juanma.ecommerce.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotosRepository extends JpaRepository<StuffPhoto, Long> {
    StuffPhoto findByStuff(long stuff);
}