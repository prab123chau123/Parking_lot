package com.parkingsystem.parkingservice;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.parkingsystem.Model.ParkingRecord;
import com.parkingsystem.Model.ParkingSlot;
import com.parkingsystem.Model.Vehicle;
import com.parkingsystem.Repository.ParkingRecordRepository;
import com.parkingsystem.Repository.ParkingSlotRepository;
import com.parkingsystem.Repository.VehicleRepository;

@Service
public class ParkingService {

	@Autowired
	private ParkingRecordRepository parkingRecordRepository;

	@Autowired
	private ParkingSlotRepository parkingSlotRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	public ResponseEntity<?> parkVehicle(String vehicleNumber, String vehicleType, String ownerName,
			String ownerContact, String slotNumber) {
		try {
			Vehicle vehicle = this.vehicleRepository.findByVehicleNumber(vehicleNumber);
			if (vehicle == null) {
				vehicle = new Vehicle();
				vehicle.setVehicleNumber(vehicleNumber);
				vehicle.setVehicleType(vehicleType);
				vehicle.setOwnerName(ownerName);
				vehicle.setOwnerContact(ownerContact);
				this.vehicleRepository.save(vehicle);
			}
			ParkingSlot parkingSlot = this.parkingSlotRepository.findBySlotNumber(slotNumber);
			if (parkingSlot == null | parkingSlot.isOccupied()
					|| !parkingSlot.getAllowedVehicleType().equalsIgnoreCase(vehicle.getVehicleType())) {
				throw new IllegalStateException("Parking slot not available or not suitable for vehicle type");
			}
			parkingSlot.setOccupied(true);
			this.parkingSlotRepository.save(parkingSlot);

			ParkingRecord parkingRecord = new ParkingRecord();
			parkingRecord.setVehicle(vehicle);
			parkingRecord.setParkingSlot(parkingSlot);
			parkingRecord.setEntryTime(LocalDateTime.now());
			this.parkingRecordRepository.save(parkingRecord);

			return new ResponseEntity<String>("Vehicle is Parked successfully "+vehicleNumber, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Something went wrong try again", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> checkoutVehicle(String vehicleNumber) {
		try {
			Vehicle vehicle = this.vehicleRepository.findByVehicleNumber(vehicleNumber);
			if (vehicle == null) {
				throw new IllegalStateException("Vehicle not found");
			}
			Optional<ParkingRecord> records = this.parkingRecordRepository.findAll().stream()
					.filter(record -> record.getVehicle().equals(vehicle) && record.getExitTime() == null).findFirst();
			if (!records.isPresent()) {
				throw new IllegalStateException("No active parking record is found for this vehicle");
			}
			ParkingRecord parkingRecord = records.get();
			parkingRecord.setExitTime(LocalDateTime.now());
			long totalTime = ChronoUnit.MINUTES.between(parkingRecord.getEntryTime(), parkingRecord.getExitTime());
			parkingRecord.setTotalTime(totalTime);
			Double fare = calculateFare(totalTime);
			parkingRecord.setFare(fare);

			ParkingSlot parkingSlot = parkingRecord.getParkingSlot();
			parkingSlot.setOccupied(false);
			this.parkingSlotRepository.save(parkingSlot);
			this.parkingRecordRepository.save(parkingRecord);
			return new ResponseEntity<String>("Checkout completed and total fare is "+parkingRecord.getFare(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("Something went wrong try again", HttpStatus.BAD_REQUEST);
	}

	private Double calculateFare(long totaltime) {
		return totaltime * 2.06;
	}
}
