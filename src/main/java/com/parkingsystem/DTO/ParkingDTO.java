package com.parkingsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ParkingDTO {
	private String vehicleNumber;
	private String vehicleType;
	private String ownerName;
	private String ownerContact;
	private String slotNumber;
}
