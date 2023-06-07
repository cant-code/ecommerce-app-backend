package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.exceptions.ValidationDetails;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.PARKING_SPACE;

@RestController
@RequestMapping(PARKING_SPACE)
@Tag(name = "Parking Space")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;
    private final VehicularSpaceService vehicularSpaceService;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService, VehicularSpaceService vehicularSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
        this.vehicularSpaceService = vehicularSpaceService;
    }

    @Operation(
            description = "Get All Parking Space",
            summary = "Get All Parking Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
    })
    @RolesAllowed("ADMIN")
    @GetMapping("")
    public List<ParkingSpace> getAllParkingSpace() {
        return parkingSpaceService.getAllParkingSpace();
    }

    @Operation(
            description = "Get Parking space",
            summary = "Get Parking space"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/{id}")
    public ParkingSpace getParkingSpace(@PathVariable long id,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                LocalDateTime startDate,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                    LocalDateTime endDate) {
        return parkingSpaceService.getParkingSpace(startDate, endDate, id);
    }

    @Operation(
            description = "Get Parking Space by name",
            summary = "Get Parking Space"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/byName/{name}")
    public ParkingSpace getParkingSpace(@PathVariable String name) {
        return parkingSpaceService.getParkingSpace(name);
    }

    @Operation(
            description = "Add Parking Space",
            summary = "Add Parking Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
    })
    @PostMapping("/add")
    public ParkingSpace addParkingSpace(@Valid @RequestBody ParkingSpace space) {
        return parkingSpaceService.saveParkingSpace(space);
    }

    @Operation(
            description = "Edit Parking Space",
            summary = "Edit Parking Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @PutMapping("/edit")
    public ParkingSpace editParkingSpace(@Valid @RequestBody ParkingSpace space) {
        return parkingSpaceService.updateParkingSpace(space);
    }

    @Operation(
            description = "Delete Parking Space using ID",
            summary = "Delete Parking Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @DeleteMapping("/{id}/delete")
    public void deleteParkingSpace(@Valid @PathVariable long id) {
        parkingSpaceService.deleteParkingSpace(id);
    }

    @Operation(
            description = "Add Vehicular Space to Parking Space using Name",
            summary = "Add Vehicular Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @PostMapping("/byName/{name}/vehicularspace/add")
    public VehicularSpace addVehicularSpace(@Valid @PathVariable String name, @Valid @RequestBody VehicularSpace vehicularSpace) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(name);
        vehicularSpace.setParkingSpace(parkingSpace);
        return vehicularSpaceService.saveVehicularSpace(vehicularSpace);
    }

    @Operation(
            description = "Add Vehicular Space to Parking Space using ID",
            summary = "Add Vehicular Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @PostMapping("/{id}/vehicularspace/add")
    public VehicularSpace addVehicularSpace(@Valid @PathVariable long id, @Valid @RequestBody VehicularSpace vehicularSpace) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(id);
        vehicularSpace.setParkingSpace(parkingSpace);
        return vehicularSpaceService.saveVehicularSpace(vehicularSpace);
    }
}
