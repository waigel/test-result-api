package com.waigel.testresultapi.controllers

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.models.CreateTenantRequestDTO
import com.waigel.testresultapi.services.TenantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping(Constants.API_TENANT_CONTROLLER_PATH)
@Tag(name = "Tenant API", description = "This API is used to manage tenants")
class TenantController(
    private val tenantService: TenantService
) {

    private val logger = LoggerFactory.getLogger(TenantController::class.java)

    @PostMapping(value = [""])
    @Operation(summary = "Create a new tenant")
    fun createTenant(
        @Valid @RequestBody createTenantRequest: CreateTenantRequestDTO,
        @PathVariable companyId: UUID,
    ) {
        logger.info("Create new tenant for company $companyId, request = $createTenantRequest")
        tenantService.create(createTenantRequest, companyId)

    }
}