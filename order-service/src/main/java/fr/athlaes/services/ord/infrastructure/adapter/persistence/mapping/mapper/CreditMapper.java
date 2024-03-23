package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.mapper;

import java.util.List;

import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.CreditOrderEntity;

public class CreditMapper {
    public static List<CreditOrder> toDomain(List<CreditOrderEntity> creditOrderEntities) {
        return creditOrderEntities.stream()
                .map(CreditOrderEntity::toDomain)
                .toList();
    }

    public static List<CreditOrderEntity> toEntity(List<CreditOrder> creditOrders) {
        return creditOrders.stream()
                .map((CreditOrder s) -> new CreditOrderEntity(s))
                .toList();
    }
}
