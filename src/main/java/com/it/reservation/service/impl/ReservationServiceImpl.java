package com.it.reservation.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it.reservation.dto.request.ReservationReqDTO;
import com.it.reservation.dto.response.ReservationResDTO;
import com.it.reservation.dto.response.SeatTypeResDTO;
import com.it.reservation.payload.CustomerUserAttr;
import com.it.reservation.repository.ReservationHistoryRepository;
import com.it.reservation.repository.ReservationRepository;
import com.it.reservation.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService{

	
    @Autowired
    ModelMapper mapper;
    
    @Autowired
    ReservationRepository reservationRepository;
    
    @Autowired
    ReservationHistoryRepository reservationHistoryRepository;

	@Override
	public ReservationResDTO getRevById(Long revId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReservationResDTO getRevByUserId(Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReservationResDTO> getRevAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long saveRev(CustomerUserAttr userAttr, ReservationReqDTO req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long updateRev(CustomerUserAttr userAttr, ReservationReqDTO req, Long revId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRev(Long revId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String checkMaximumCancelRev(Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReservationResDTO> getRevHistoryByUserId(Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReservationResDTO> getRevHistoryAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SeatTypeResDTO> getSeatTypeAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
    
	

}
