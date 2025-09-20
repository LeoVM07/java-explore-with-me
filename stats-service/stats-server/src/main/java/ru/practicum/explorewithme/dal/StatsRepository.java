package ru.practicum.explorewithme.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ServiceHitForListDto;
import ru.practicum.explorewithme.model.ServiceHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<ServiceHit, Long> {


    @Query("SELECT new ru.practicum.dto.ServiceHitForListDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM ServiceHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ServiceHitForListDto> findAllByTimestampBetweenAndUriIn(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("SELECT new ru.practicum.dto.ServiceHitForListDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM ServiceHit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<ServiceHitForListDto> findAllUniqueIpAndTimestampBetweenAndUriIn(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}
