package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.BatchInfo;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Optional;

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

    public String generateBatchReference(LocalDate localdate) {

        System.out.println("Batch info....1");

        String prefix = "B" + String.format("%02d", localdate.getYear())
                .substring(2) + String.format("%02d", localdate.getMonthValue()) + String.format("%02d", localdate.getDayOfMonth());
        System.out.println("Batch size...." + find("order by id desc limit 1").list().await().indefinitely());

        return find("order by id desc limit 1").list().onItem().ifNotNull().transform(batchInfos -> {

            System.out.println("Batch info....");
            String lastInv = batchInfos.get(0).batchReference.substring(prefix.length());

            int lastInvInt = Integer.parseInt(lastInv) + 1;
            String finalInv = String.format("%03d", lastInvInt);

            return prefix + finalInv;
        }).onItem().ifNull().continueWith(() -> {

            System.out.println("Batch info....");
            String finalInv = String.format("%03d", 1);

            return prefix + finalInv;
        }).await().indefinitely();

    }

}
