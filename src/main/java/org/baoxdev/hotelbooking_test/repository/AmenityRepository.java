package org.baoxdev.hotelbooking_test.repository;

import org.baoxdev.hotelbooking_test.model.entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenities , String> {

    Object findAmenitiesByAmenityIdAndIsDeletedFalse(String amenityId);

    Amenities findByAmenityName(String amenityName);
}
