package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.BatchInfo;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BatchInfoRepository implements PanacheRepository<BatchInfo> {

    public Uni<BatchInfo> validateRequest(String batchNumber) {

        return list("batchNumber = ?1", batchNumber).onItem().transform(batchInfo -> {
            if (batchInfo.isEmpty()) {
                return null;

            } else {
                return batchInfo.get(0);

            }
        });
    }

}
