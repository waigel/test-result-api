package com.waigel.testresultapi.repositories

import com.waigel.testresultapi.entities.TenantCompany
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface TenantCompanyRepository : JpaRepository<TenantCompany, UUID> {
}