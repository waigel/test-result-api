package com.waigel.testresultapi.repositories

import com.waigel.testresultapi.entities.TenantCompany
import com.waigel.testresultapi.entities.TenantLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface TenantLocationRepository : JpaRepository<TenantLocation, UUID> {
}