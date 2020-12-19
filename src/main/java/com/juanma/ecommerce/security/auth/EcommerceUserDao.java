package com.juanma.ecommerce.security.auth;

import java.util.Optional;

public interface EcommerceUserDao {
    Optional<EcommerceUser> selectApplicationUserByUsername(String username);
}
