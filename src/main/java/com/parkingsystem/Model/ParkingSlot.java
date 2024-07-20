package com.parkingsystem.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table
@Entity
public class ParkingSlot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String slotNumber;
	private boolean isOccupied;
	private String allowedVehicleType;
	private String location;
	
	@OneToMany(mappedBy = "parkingSlot",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<ParkingRecord> parkingRecords;
}
