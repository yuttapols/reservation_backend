package com.it.reservation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.it.reservation.dto.request.ReservationReqDTO;
import com.it.reservation.dto.response.ReservationResDTO;
import com.it.reservation.dto.response.SeatTypeResDTO;
import com.it.reservation.entities.ReservationEntities;
import com.it.reservation.entities.SeatTypeEntities;
import com.it.reservation.payload.CustomerUserAttr;
import com.it.reservation.repository.ReservationHistoryRepository;
import com.it.reservation.repository.ReservationRepository;
import com.it.reservation.repository.SeatTypeRepository;
import com.it.reservation.service.ReservationService;
import com.it.reservation.util.Constants;
import com.it.reservation.util.DateUtil;

@Service
public class ReservationServiceImpl implements ReservationService{

	
    @Autowired
    ModelMapper mapper;
    
    @Autowired
    ReservationRepository reservationRepository;
    
    @Autowired
    ReservationHistoryRepository reservationHistoryRepository;
    
    @Autowired
    SeatTypeRepository seatTypeRepository;

	@Override
	@Transactional(readOnly = true)
	public ReservationResDTO getRevById(Long revId) throws Exception {
		ReservationResDTO resp = null;
		
		if(null != revId) {
			Optional<ReservationEntities> revOpt = reservationRepository.findById(revId);
			if(revOpt.isPresent()) {
				resp = mapper.map(revOpt.get(), new TypeToken<ReservationResDTO>() {
                }.getType());
			}
		}
		return resp;
	}

	@Override
	@Transactional(readOnly = true)
	public ReservationResDTO getRevByUserId(Long userId) throws Exception {
		ReservationResDTO resp = null;
		
		if(null != userId) {
			Optional<ReservationEntities> revOpt = reservationRepository.findByUserId(userId);
			if(revOpt.isPresent()) {
				resp = mapper.map(revOpt.get(), new TypeToken<ReservationResDTO>() {
                }.getType());
			}
		}
		return resp;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReservationResDTO> getRevAll() throws Exception {
		List<ReservationResDTO> respAll = null;
		
		List<ReservationEntities> revAll =  reservationRepository.findAll();
		if(CollectionUtils.isNotEmpty(revAll)) {
			revAll = mapper.map(revAll, new TypeToken<List<ReservationResDTO>>() {
			}.getType());
		}
		
		return respAll;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long saveRev(CustomerUserAttr userAttr, ReservationReqDTO req) throws Exception {
		Long resp = null;
		
		if(ObjectUtils.isNotEmpty(req)) {
			
			ReservationEntities revEntities = new ReservationEntities();
			Map<String, Integer> map = this.getRevNoCal();
			revEntities.setRevNo(this.getKeyMap(map));
			revEntities.setRevNoNumber(Long.valueOf(this.getValueMap(map)));
			revEntities.setSeatTypeId(req.getSeatTypeId());
			revEntities.setUserId(req.getUserId());
			revEntities.setRevStatus(Constants.RESERVATION.REV_STATUS_WAITING);
			revEntities.setRevTime(DateUtil.createTimestmapNow());
			revEntities.setCreateBy(userAttr.getCustomerNo());
			revEntities.setCreateDate(DateUtil.createTimestmapNow());
			revEntities = reservationRepository.saveAndFlush(revEntities);
			resp = revEntities.getId();
			
		}
		
		
		
		return resp;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long updateRev(CustomerUserAttr userAttr, ReservationReqDTO req, Long revId) throws Exception {
		Long resp = null;
		
		if(null != revId) {
			Optional<ReservationEntities> revOpt = reservationRepository.findById(revId);
			
			if(revOpt.isPresent()) {
				ReservationEntities revEntities = revOpt.get();
				revEntities.setRevStatus(req.getRevStatus());
				revEntities.setUpdateBy(userAttr.getCustomerNo());
				revEntities.setUpdateDate(DateUtil.createTimestmapNow());
				revEntities = reservationRepository.saveAndFlush(revEntities);
				resp = revEntities.getId();
			}
		}
		
		return resp;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteRev(Long revId) throws Exception {
		reservationRepository.deleteById(revId);
		
	}

	@Override
	public String checkMaximumCancelRev(Long userId) throws Exception {
		String resp = Constants.STATUS_CODE_SUCESS;
		
		if(Constants.RESERVATION.REV_CANCEL_MAXIMUM < reservationRepository.findMaxCancelByUserIdInDay(userId)) {
			resp = Constants.STATUS_CODE_UNSUCESS;
		}
		
		return resp;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SeatTypeResDTO> getSeatTypeAll() throws Exception {
		 List<SeatTypeResDTO> seatTypeAll = null;
		 
		 List<SeatTypeEntities> seatEntitiesList = seatTypeRepository.findAll();
		 if(CollectionUtils.isNotEmpty(seatEntitiesList)) {
			 seatTypeAll = mapper.map(seatEntitiesList, new TypeToken<List<SeatTypeResDTO>>() {
				}.getType());
		 }
		 
		return seatTypeAll;
	}
	
	
	@Transactional(readOnly = true)
	public Map<String, Integer> getRevNoCal() {

		
		String revNoBefore1Day = reservationRepository.findRevNoBeforeDay(DateUtil.createDateTime(-1));
		
		for(String revNo : Constants.RESERVATION.REV_NO_ALL) {
			if(!revNo.equalsIgnoreCase(revNoBefore1Day)) {
				Integer revCount = reservationRepository.findMaxUsedRevNoInDay(revNo);
				if(Constants.RESERVATION.REV_MAXIMUM >= revCount) {
					Map<String, Integer> map = new HashMap<String, Integer>();
					map.put(revNo, (revCount+1));
					return map;
				}
			}
		}
		return new HashMap<String, Integer>();
	}
	
	public String getKeyMap(Map<String, Integer> map) {
	    return map.entrySet().stream().filter(f-> null != f).map(Map.Entry::getKey).findFirst().orElse(null);
	}
	
	public Integer getValueMap(Map<String, Integer> map) {
	    return map.entrySet().stream().filter(f-> null != f).map(Map.Entry::getValue).findFirst().orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getRevByStatusWaiting() throws Exception {

		return reservationRepository.findUserStatusWaitingAll();
	}

	@Override
	public List<ReservationResDTO> getAllRevByUserId(Long userId) throws Exception {
		List<ReservationResDTO> respAll = null;
		
		List<ReservationEntities> revAll =  reservationRepository.findByByUserId(userId);
		if(CollectionUtils.isNotEmpty(revAll)) {
			respAll = mapper.map(revAll, new TypeToken<List<ReservationResDTO>>() {
			}.getType());
		}
		
		return respAll;
	}

	@Override
	public List<ReservationResDTO> getAllRevByStatusWaiting() throws Exception {
		List<ReservationResDTO> respAll = null;
		
		List<ReservationEntities> revAll =  reservationRepository.findByStatusWaiting();
		if(CollectionUtils.isNotEmpty(revAll)) {
			respAll = mapper.map(revAll, new TypeToken<List<ReservationResDTO>>() {
			}.getType());
		}
		
		return respAll;
	}
    
    
	

}
