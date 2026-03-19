package com.tms.repository;

import com.tms.model.entity.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends AbstractRepository<Status, Integer> {

    @Query("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = :name")
    List<Status> findProjectStatusByName(@Param("name") String name);

    @Query("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS'")
    List<Status> findProjectStatus();

    default Status find(String name) {
        List<Status> list = findProjectStatusByName(name);
        if (list.isEmpty()) {
            throw new RuntimeException("Here is no status with enetered name!");
        }
        return list.get(0);
    }

    default Status findByProjectStatusAndReadyToWork() {
        return find("READY_TO_WORK");
    }

    default Status findByProjectStatusAndAssigned() {
        return find("ASSIGNED");
    }

    default Status READY_TO_WORK() {
        return find("READY_TO_WORK");
    }

    default Status ASSIGN_TRANSLATOR() {
        return find("ASSIGNED");
    }

    default Status INFORM_TRANSLATOR() {
        return find("INFORMED");
    }

    default Status DELIVERED() {
        return find("DELIVERED");
    }

    default Status PAID() {
        return find("PAID");
    }

    default Status CLIENT_PAID() {
        return find("CLIENT_PAID");
    }

    default Status TRANSLATOR_PAID() {
        return find("TRANSLATOR_PAID");
    }

    default Status INVOICED() {
        return find("INVOICED");
    }
}

