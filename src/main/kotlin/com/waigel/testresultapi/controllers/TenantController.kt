package com.waigel.testresultapi.controllers

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.models.CreateTenantRequestDTO
import com.waigel.testresultapi.models.TenantLocationRequest
import com.waigel.testresultapi.services.TenantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
    ): Tenant {
        logger.info("Create new tenant for company $companyId, request = $createTenantRequest")
        return tenantService.create(createTenantRequest, companyId)
    }

    @GetMapping("")
    @Operation(summary = "Get a list of all tenants - admin")
    fun getTenants(@PathVariable companyId: UUID): List<Tenant> {
        logger.info("Get a list of all tenants for company = $companyId")
        return tenantService.getAllByCompanyId(companyId)
    }

    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant by companyId and tenantId - admin")
    fun getTenantByIdAndCompanyId(@PathVariable companyId: UUID, @PathVariable tenantId: UUID): Tenant {
        logger.info("Get tenant by companyId = $companyId and tenantId = $tenantId")
        return tenantService.getByIdAndCompanyId(tenantId, companyId)
    }

    @PatchMapping(value = ["/{tenantId}"])
    @Operation(summary = "Update a tenant")
    fun updateTenant(
        @PathVariable companyId: UUID,
        @PathVariable tenantId: UUID,
        @Valid @RequestBody tenantRequest: TenantLocationRequest
    ): Tenant {
        logger.info("Update tenant with id $tenantId")
        return tenantService.update(tenantId, companyId, tenantRequest)
    }
}