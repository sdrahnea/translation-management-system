package com.tms.repository;

import com.tms.model.entity.Client;
import com.tms.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends AbstractRepository<Client, Integer> {

    Optional<Client> findByUser(User user);

    default Client getClient(final User user) {
        return findByUser(user).orElse(null);
    }
}

