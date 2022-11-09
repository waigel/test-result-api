package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.exceptions.Message
import com.waigel.testresultapi.exceptions.NotFoundException
import com.waigel.testresultapi.models.CreateTenantRequestDTO
import com.waigel.testresultapi.repositories.TenantRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TenantService(
    private val tenantRepository: TenantRepository,
    private val tenantCompanyService: TenantCompanyService,
    private val tenantLocationService: TenantLocationService
) {
    private val logger = LoggerFactory.getLogger(TenantService::class.java)

    fun getById(id: UUID): Tenant {
        return tenantRepository.findById(id).orElseThrow {
            logger.info("Tenant with id $id not found")
            throw NotFoundException(Message.RESOURCE_NOT_FOUND)
        }
    }

    /**
     * Delete a Tenant if exist
     * @param tenantId The id of the tenant to delete
     */
    fun delete(tenantId: String) {
        val tenant = getById(UUID.fromString(tenantId))
        this.delete(tenant)
    }

    /**
     * Delete a Tenant if exist
     * @param tenant The tenant to delete
     */
    fun delete(tenant: Tenant) {
        logger.info("Delete tenant with id ${tenant.id}")
        tenantRepository.delete(tenant)
    }

    /**
     * Creates a new Tenant.
     * @param createTenantRequest The request to create a new tenant
     * @param companyId The id of the company to which the tenant belongs
     * @return The created tenant
     */
    fun create(createTenantRequest: CreateTenantRequestDTO, companyId: UUID): Tenant {
        val tenantCompany = tenantCompanyService.getById(companyId)
        logger.info("Create new tenant, request = $createTenantRequest")

        val location = tenantLocationService.create(createTenantRequest)
        return tenantRepository.save(Tenant.from(tenantCompany, location))
    }

    /**
     * Find all tenants by companyId - empty list if no result found
     * @param companyId The id of the company to which the tenant belongs
     * @return A list of tenants
     */
    fun getAllByCompanyId(companyId: UUID): List<Tenant> {
        return tenantRepository.findAllByCompanyId(companyId)
    }

    /**
     * Find tenant by companyId and tenantId - throw NotFoundException if no tenant found
     * @param tenantId The id of the tenant to find
     * @param companyId The id of the company to find
     * @return The tenant found
     * @throws NotFoundException if no tenant found
     */
    fun getByIdAndCompanyId(tenantId: UUID, companyId: UUID): Tenant {
        return tenantRepository.findByIdAndCompanyId(tenantId, companyId)
            .orElseThrow {
                logger.info("Tenant with id $tenantId not found")
                throw NotFoundException(Message.RESOURCE_NOT_FOUND)
            }
    }
}