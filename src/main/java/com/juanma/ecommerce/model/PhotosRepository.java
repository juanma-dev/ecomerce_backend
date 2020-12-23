package com.juanma.ecommerce.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotosRepository extends JpaRepository<StuffPhoto, Long> {
    Optional<StuffPhoto> findByStuff(long stuff);
}