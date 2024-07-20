package com.parkingsystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parkingsystem.Model.ParkingRecord;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long>{

}
