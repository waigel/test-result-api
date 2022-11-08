package com.waigel.testresultapi.controllers

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.models.TenantCompanyRequestDTO
import com.waigel.testresultapi.services.TenantCompanyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping(Constants.API_TENANT_COMPANY_CONTROLLER_PATH)
@Tag(name = "Tenant Company API", description = "This API is used to manage tenant companies")
class TenantCompanyController(
    private val tenantCompanyService: TenantCompanyService
) {
    private val logger = LoggerFactory.getLogger(TenantCompanyController::class.java)

    @PostMapping(value = [""])
    @Operation(summary = "Create a new tenant company")
    fun createTenantCompany(
        @Valid @RequestBody createTenantCompanyRequest: TenantCompanyRequestDTO
    ) {
        logger.info("Create new tenant company")
        tenantCompanyService.create(createTenantCompanyRequest)
    }

    @PatchMapping(value = ["/{tenantCompanyId}"])
    @Operation(summary = "Update a tenant company")
    fun updateTenantCompany(
        @PathVariable tenantCompanyId: UUID,
        @Valid @RequestBody tenantCompanyRequest: TenantCompanyRequestDTO
    ) {
        logger.info("Update tenant company with id $tenantCompanyId")
        tenantCompanyService.update(tenantCompanyId, tenantCompanyRequest)
    }

    @DeleteMapping(value = ["/{id}"])
    @Operation(summary = "Delete a tenant company")
    fun deleteTenantCompany(@PathVariable("id") tenantCompanyId: UUID) {
        logger.info("Delete tenant company")
        tenantCompanyService.delete(tenantCompanyId)
    }

}