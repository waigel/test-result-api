package com.waigel.testresultapi.repositories

import com.waigel.testresultapi.entities.Tenant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID


@Repository
interface TenantRepository : JpaRepository<Tenant, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<Tenant>
    fun findByIdAndCompanyId(tenantId: UUID, companyId: UUID) : Optional<Tenant>
}