package com.italohreis.medly.controllers;

import com.italohreis.medly.dtos.availability.AvailabilityRequestDTO;
import com.italohreis.medly.dtos.availability.AvailabilityResponseDTO;
import com.italohreis.medly.exceptions.ForbiddenException;
import com.italohreis.medly.mappers.AvailabilityMapper;
import com.italohreis.medly.models.Availability;
import com.italohreis.medly.models.User;
import com.italohreis.medly.services.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    private final AvailabilityMapper availabilityMapper;

    @PostMapping
    public ResponseEntity<?> createAvailability(
            @RequestBody @Valid AvailabilityRequestDTO availabilityRequestDTO,
            Authentication authentication) {

        checkOwnership(authentication, availabilityRequestDTO.doctorId());
        Availability availability = availabilityMapper.toModel(availabilityRequestDTO);
        return ResponseEntity.ok(availabilityService.createAvailability(availability));
    }

    @GetMapping
    public ResponseEntity<?> getAvailabilitiesByDoctorId(
            @RequestParam("doctorId") UUID doctorId,
            Authentication authentication) {

        checkOwnership(authentication, doctorId);

        List<Availability> availabilities = availabilityService.getAvailabilitiesByDoctorId(doctorId);

        List<AvailabilityResponseDTO> responseDTOs = availabilities.stream()
                .map(availabilityMapper::toDto)
                .toList();

        return ResponseEntity.ok(responseDTOs);
    }

    private void checkOwnership(Authentication authentication, UUID requestedDoctorId) {
        User loggedInUser = (User) authentication.getPrincipal();

        if (loggedInUser.getDoctor() == null || !loggedInUser.getDoctor().getId().equals(requestedDoctorId)) {
            throw new ForbiddenException("Access denied. You do not own this resource.");
        }
    }
}
