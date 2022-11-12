package com.waigel.testresultapi.repositories

import com.waigel.testresultapi.entities.CwaTransmissionDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CwaTransmissionDetailsRepository : JpaRepository<CwaTransmissionDetails, UUID> {
}