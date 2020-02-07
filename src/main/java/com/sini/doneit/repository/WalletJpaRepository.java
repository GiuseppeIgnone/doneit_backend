package com.sini.doneit.repository;

import com.sini.doneit.model.User;
import com.sini.doneit.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<Wallet, Long> {
}
