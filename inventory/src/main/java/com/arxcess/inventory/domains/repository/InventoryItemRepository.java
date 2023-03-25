package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.InventoryItem;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InventoryItemRepository implements PanacheRepository<InventoryItem> {

    public Uni<InventoryItem> getById(Long id) {

        return findById(id);
//        return findById(id).onItem()
//                .ifNull()
//                .failWith(new NotFoundException("User not found!"))
//                .onFailure()
//                .transform(throwable -> new NotFoundException(throwable.getMessage()));

    }

}
