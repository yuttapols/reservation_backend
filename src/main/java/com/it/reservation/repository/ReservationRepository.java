package com.it.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.it.reservation.entities.ReservationEntities;

public interface ReservationRepository extends JpaRepository<ReservationEntities, Long>{

}
