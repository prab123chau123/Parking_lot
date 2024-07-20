package com.parkingsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parkingsystem.Model.ParkingSlot;



@Repository
public interface ParkingSlotRepository  extends JpaRepository<ParkingSlot, Long>{
	
	ParkingSlot findBySlotNumber(String slotNumber);

}
