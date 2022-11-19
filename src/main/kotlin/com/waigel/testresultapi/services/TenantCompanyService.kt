package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.TenantCompany
import com.waigel.testresultapi.exceptions.Message
import com.waigel.testresultapi.exceptions.NotFoundException
import com.waigel.testresultapi.models.TenantCompanyRequestDTO
import com.waigel.testresultapi.repositories.TenantCompanyRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class TenantCompanyService(
    private val tenantCompanyRepository: TenantCompanyRepository,
) {

    private val logger = LoggerFactory.getLogger(TenantCompanyService::class.java)

    fun getById(id: UUID): TenantCompany {
        return tenantCompanyRepository.findById(id).orElseThrow {
            logger.info("TenantCompany with id $id not found")
            throw NotFoundException(Message.RESOURCE_NOT_FOUND)
        }
    }


    /**
     * Creates a new TenantCompany.
     * @param request The request containing the data for the new TenantCompany.
     * @return The created TenantCompany.
     */
    @Transactional
    fun create(request: TenantCompanyRequestDTO): TenantCompany {
        logger.info("Create new tenant company, request = $request")
        return tenantCompanyRepository.save(TenantCompany.from(request))
    }

    /**
     * Update a TenantCompany if exist
     * @param id The id of the tenant company to update
     * @param request The request containing the data for the TenantCompany.
     * @return The updated TenantCompany.
     */
    @Transactional
    fun update(id: UUID, request: TenantCompanyRequestDTO): TenantCompany {
        logger.info("Update tenant company $id, request = $request")
        val tenantCompany = getById(id)
        return tenantCompanyRepository.save(tenantCompany.apply {
            this.name = request.name
            this.street = request.street
            this.houseNumber = request.houseNumber
            this.zipcode = request.zipcode
            this.city = request.city
            this.country = request.country
        })
    }

    fun delete(tenantCompanyId: UUID) {
        logger.info("Delete tenant company $tenantCompanyId")
        tenantCompanyRepository.deleteById(tenantCompanyId)
    }


}