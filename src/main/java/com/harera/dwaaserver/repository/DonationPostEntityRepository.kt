package com.harera.dwaaserver.repository;

import com.harera.dwaaserver.entity.DonationPostEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DonationPostEntityRepository : JpaRepository<DonationPostEntity, Int> {

    @Query("SELECT * FROM donation_post WHERE uid = :uid", nativeQuery = true)
    fun getDonationPostsByUid(uid: String): List<DonationPostEntity>
}