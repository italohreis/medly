package com.italohreis.medly.repositories;

import com.italohreis.medly.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
    List<Availability> findByDoctorId(UUID doctorId);
}
