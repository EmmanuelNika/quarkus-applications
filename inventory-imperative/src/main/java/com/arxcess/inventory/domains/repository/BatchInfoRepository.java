package com.arxcess.inventory.domains.repository;

import com.arxcess.inventory.domains.BatchInfo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Optional;

@ApplicationScoped
public class BatchInfoRepository implements PanacheRepository<BatchInfo> {

    public Optional<BatchInfo> validateRequestOptional(String batchNumber) {

        return find("batchNumber = ?1", batchNumber).singleResultOptional();

    }

    public String generateBatchReference(LocalDate localdate) {

        String prefix = "B" + String.format("%02d", localdate.getYear())
                .substring(2) + String.format("%02d", localdate.getMonthValue()) + String.format("%02d", localdate.getDayOfMonth());

        Optional<BatchInfo> lastItem = find("order by id").firstResultOptional();

        if (lastItem.isPresent()) {

            String lastInv = lastItem.get().batchReference.substring(prefix.length());

            int lastInvInt = Integer.parseInt(lastInv) + 1;
            String finalInv = String.format("%03d", lastInvInt);

            return prefix + finalInv;

        } else {

            String finalInv = String.format("%03d", 1);

            return prefix + finalInv;
        }

    }

}
