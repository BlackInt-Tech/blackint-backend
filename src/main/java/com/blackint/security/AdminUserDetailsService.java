package com.blackint.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.blackint.entity.AdminUser;
import com.blackint.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AdminUser admin = repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Admin login attempt with unknown email: {}", email);
                    return new UsernameNotFoundException("Admin not found");
                });

        log.info("Admin authenticated: {}", email);

        return admin;
    }
}