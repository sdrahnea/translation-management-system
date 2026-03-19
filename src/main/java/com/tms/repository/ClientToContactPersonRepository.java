package com.tms.repository;

import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientToContactPersonRepository extends AbstractRepository<ClientToContactPerson, Integer> {

    List<ClientToContactPerson> findByClientIdAndPersonId(Integer clientId, Integer personId);

    long deleteByClientId(Integer clientId);

    default ClientToContactPerson findBy(final Client client, final Person person) {
        List<ClientToContactPerson> result = findByClientIdAndPersonId(client.getId(), person.getId());
        return result.isEmpty() ? new ClientToContactPerson(client, person) : result.get(0);
    }

    default void removeBy(final Client client) {
        deleteByClientId(client.getId());
    }
}
