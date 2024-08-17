package com.it.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.it.reservation.entities.ReservationHistoryEntities;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistoryEntities, Long>{

}
