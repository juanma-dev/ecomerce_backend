package com.juanma.ecommerce.security.auth;

import com.juanma.ecommerce.model.Authority;
import com.juanma.ecommerce.model.User;
import com.juanma.ecommerce.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository("jpa")
public class JpaEcommerceUserDao implements EcommerceUserDao{
    @Autowired
    UserRepository repository;
    @Override
    public Optional<EcommerceUser> selectApplicationUserByUsername(String username) {
        Optional<User> optionalUser = repository.findByUname(username);
        User user = optionalUser.orElseThrow(InvalidParameterException::new);
        return Optional.of(new EcommerceUser(
                user.getUname(),
                user.getPassword(),
                getGrantedAuthorities(user.getAuthorities()),
                true,
                true,
                true,
                true
                ));
    }

    private Set<SimpleGrantedAuthority> getGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAname()))
                .collect(Collectors.toSet());
    }
}
