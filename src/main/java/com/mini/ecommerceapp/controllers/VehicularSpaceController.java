package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.exceptions.ValidationDetails;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.VEHICLE_SPACE;

@RestController
@RequestMapping(VEHICLE_SPACE)
@Tag(name = "Vehicle Space")
public class VehicularSpaceController {
    private final VehicularSpaceService vehicularSpaceService;

    @Autowired
    public VehicularSpaceController(VehicularSpaceService vehicularSpaceService) {
        this.vehicularSpaceService = vehicularSpaceService;
    }

    @Operation(
            description = "Get All Vehicle Space",
            summary = "Get All Vehicle Space",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
    })
    @RolesAllowed("ADMIN")
    @GetMapping("")
    public List<VehicularSpace> getAllVehicularSpace() {
        return vehicularSpaceService.getAllVehicularSpace();
    }

    @Operation(
            description = "Get Vehicle space",
            summary = "Get Vehicle space"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/{id}")
    public VehicularSpace getVehicularSpace(@Valid @PathVariable long id) {
        return vehicularSpaceService.getVehicularSpace(id);
    }

    @Operation(
            description = "Edit Vehicle Space",
            summary = "Edit Vehicle Space",
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
    public VehicularSpace updateVehicularSpace(@Valid @RequestBody VehicularSpace vehicularSpace) {
        return vehicularSpaceService.updateVehicularSpace(vehicularSpace);
    }

    @Operation(
            description = "Delete Vehicle Space using ID",
            summary = "Delete Vehicle Space",
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
    public void deleteVehicularSpace(@Valid @PathVariable long id) {
        vehicularSpaceService.deleteVehicularSpace(id);
    }

    @Operation(
            description = "Get Vehicle space using Parking Space and name",
            summary = "Get Vehicle space"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/{parkingSpace}/{name}")
    public VehicularSpace getVehicularSpace(@Valid @PathVariable String parkingSpace, @Valid @PathVariable String name) {
        return vehicularSpaceService.getVehicularSpace(parkingSpace, name);
    }
}
