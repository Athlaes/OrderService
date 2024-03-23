package fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.mapper;

import java.util.List;

import fr.athlaes.services.ord.domain.StatusHistory;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.StatusHistoryEntity;

public class StatusMapper {
    public static List<StatusHistory> toDomain(List<StatusHistoryEntity> statusUpdatesEntities) {
        return statusUpdatesEntities.stream()
                .map(StatusHistoryEntity::toDomain)
                .toList();
    }

    public static List<StatusHistoryEntity> toEntity(List<StatusHistory> statusUpdates) {
        return statusUpdates.stream()
                .map((StatusHistory s) -> new StatusHistoryEntity(s))
                .toList();
    }
}
