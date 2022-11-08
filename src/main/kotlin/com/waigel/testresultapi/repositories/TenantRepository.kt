package com.waigel.testresultapi.repositories

import com.waigel.testresultapi.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface TenantRepository : JpaRepository<Tenant, UUID> {
}