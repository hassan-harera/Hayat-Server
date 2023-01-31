package com.harera.hayat.repository.medicine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harera.hayat.model.donation.medicine.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

}
