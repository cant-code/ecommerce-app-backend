package com.mini.ecommerceapp;

import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.services.AreaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class EcommerceappApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceappApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AreaService areaService) {
		return args -> {
			ParkingSpace p1 = new ParkingSpace("Mall", new ArrayList<>());
			VehicularSpace p1v1 = new VehicularSpace(
					"Two Wheelers",
					2,
					900.0d,
					p1);
			VehicularSpace p1v2 = new VehicularSpace(
					"Four Wheelers",
					20,
					2000.0d,
					p1
			);
			p1.getVehicularSpaces().add(p1v1);
			p1.getVehicularSpaces().add(p1v2);
			ParkingSpace p2 = new ParkingSpace("Hospital", new ArrayList<>());
			VehicularSpace p2v1 = new VehicularSpace(
					"Two Wheelers",
					30,
					300.0d,
					p2);
			VehicularSpace p2v2 = new VehicularSpace(
					"Four Wheelers",
					20,
					400.0d,
					p2
			);
			p2.getVehicularSpaces().add(p2v1);
			p2.getVehicularSpaces().add(p2v2);

			ParkingSpace p3 = new ParkingSpace("College", new ArrayList<>());
			VehicularSpace p3v1 = new VehicularSpace(
					"Two Wheelers",
					30,
					500.0d,
					p3);
			VehicularSpace p3v2 = new VehicularSpace(
					"Four Wheelers",
					20,
					600.0d,
					p3
			);
			p3.getVehicularSpaces().add(p3v1);
			p3.getVehicularSpaces().add(p3v2);
			ParkingSpace p4 = new ParkingSpace("Theatre", new ArrayList<>());
			VehicularSpace p4v1 = new VehicularSpace(
					"Two Wheelers",
					30,
					700.0d,
					p4);
			VehicularSpace p4v2 = new VehicularSpace(
					"Four Wheelers",
					20,
					800.0d,
					p4
			);
			p4.getVehicularSpaces().add(p4v1);
			p4.getVehicularSpaces().add(p4v2);

			Area a1 = new Area("JP Nagar", List.of(p1, p2));
			Area a2 = new Area("BTM Layout", List.of(p3, p4));
			areaService.saveArea(a1);
			areaService.saveArea(a2);
		};
	}
}
