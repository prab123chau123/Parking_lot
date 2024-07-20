package com.parkingsystem.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parkingsystem.DTO.ParkingDTO;
import com.parkingsystem.parkingservice.ParkingService;

@Controller
@RequestMapping("/parking")
public class ParkingController {

	@Autowired
	private ParkingService parkingService;

	@PostMapping("/parkVehicle")
	public ResponseEntity<?> parkVehicle(@RequestBody(required = true) ParkingDTO parkingDTO) {
		try {
			return this.parkingService.parkVehicle(parkingDTO.getVehicleNumber(), parkingDTO.getVehicleType(),
					parkingDTO.getOwnerName(), parkingDTO.getOwnerContact(), parkingDTO.getSlotNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("Internal Error", HttpStatus.BAD_GATEWAY);
	}

	@PostMapping("/checkout/{vehicleNumber}")
	public ResponseEntity<?> checkoutVehicle(@PathVariable("vehicleNumber") String vehicleNumber) {
		try {
			return this.parkingService.checkoutVehicle(vehicleNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("Internal Error", HttpStatus.BAD_GATEWAY);
	}
}
