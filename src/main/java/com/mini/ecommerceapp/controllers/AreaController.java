package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.SearchDTO;
import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.exceptions.ValidationDetailsException;
import com.mini.ecommerceapp.models.Area;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.services.AreaService;
import com.mini.ecommerceapp.services.ParkingSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.mini.ecommerceapp.utils.Constants.AREA;

@RestController
@RequestMapping(AREA)
@Tag(name = "Area")
public class AreaController {
    private final AreaService areaService;
    private final ParkingSpaceService parkingSpaceService;

    @Autowired
    public AreaController(AreaService areaService, ParkingSpaceService parkingSpaceService) {
        this.areaService = areaService;
        this.parkingSpaceService = parkingSpaceService;
    }

    @Operation(
            description = "Get all Area",
            summary = "Get all Area",
            security = { @SecurityRequirement(name = "Bearer Auth") }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("")
    public List<Area> getAllArea() {
        return areaService.getAreas();
    }

    @Operation(
            description = "Get Area by ID",
            summary = "Get Area"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/{id}")
    public Area getArea(@Valid @PathVariable long id) {
        return areaService.getArea(id);
    }

    @Operation(
            description = "Get Area by Name",
            summary = "Get Area"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) })
    })
    @GetMapping("/byName/{name}")
    public Area getArea(@Valid @PathVariable String name) {
        return areaService.getArea(name);
    }

    @Operation(
            description = "Add Area",
            summary = "Add Area",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetailsException.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @PostMapping("/add")
    public Area addArea(@Valid @RequestBody Area area) {
        return areaService.saveArea(area);
    }

    @Operation(
            description = "Edit Area",
            summary = "Edit Area",
            security = { @SecurityRequirement(name = "Bearer Auth")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetailsException.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class)) }),
    })
    @PutMapping("/edit")
    public Area updateArea(@Valid @RequestBody Area area) {
        return areaService.updateArea(area);
    }

    @Operation(
            description = "Delete Area",
            summary = "Delete Area",
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
    public void deleteArea(@Valid @PathVariable long id) {
        areaService.deleteArea(id);
    }

    @Operation(
            description = "Search for area",
            summary = "Search"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(schema = @Schema(implementation = ValidationDetailsException.class))})
    })
    @GetMapping("/search")
    public List<Area> searchArea(@Valid SearchDTO area) {
        return areaService.search(area);
    }

    @Operation(hidden = true)
    @PostMapping("/parkingspace/add")
    public void addParkingSpaceToArea(@NotNull @RequestBody Map<String, String> data) {
        ParkingSpace parkingSpace = parkingSpaceService.getParkingSpace(data.get("parkingspace"));
        areaService.addParkingSpace(data.get("area"), parkingSpace);
    }
}
