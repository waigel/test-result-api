package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.TenantLocation
import com.waigel.testresultapi.exceptions.Message
import com.waigel.testresultapi.exceptions.NotFoundException
import com.waigel.testresultapi.models.CreateTenantRequestDTO
import com.waigel.testresultapi.models.TenantLocationRequest
import com.waigel.testresultapi.repositories.TenantLocationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class TenantLocationService(
    private val tenantLocationRepository: TenantLocationRepository,
) {

    private val logger = LoggerFactory.getLogger(TenantLocationService::class.java)

    fun getById(id: UUID): TenantLocation {
        return tenantLocationRepository.findById(id).orElseThrow {
            logger.info("Tenant location with id $id not found")
            throw NotFoundException(Message.RESOURCE_NOT_FOUND)
        }
    }


    /**
     * Creates a new TenantLocation.
     * @param request The request containing the data for the new TenantLocation.
     * @return The created TenantLocation.
     */
    @Transactional
    fun create(request: TenantLocationRequest): TenantLocation {
        logger.info("Create new tenant location, request = $request")
        return tenantLocationRepository.save(TenantLocation.from(request))
    }

    @Transactional
    fun create(request: CreateTenantRequestDTO): TenantLocation {
        logger.info("Create new tenant location, request = $request")
        return tenantLocationRepository.save(TenantLocation.from(request))
    }

    /**
     * Update a TenantLocation if exist
     * @param id The id of the tenant location to update
     * @param request The request containing the data for the TenantLocation.
     * @return The updated TenantLocation.
     */
    @Transactional
    fun update(id: UUID, request: TenantLocationRequest): TenantLocation {
        logger.info("Update tenant location $id, request = $request")
        return tenantLocationRepository.save(getById(id).apply {
            this.name = request.name
            this.street = request.street
            this.houseNumber = request.houseNumber
            this.zipcode = request.zipcode
            this.city = request.city
            this.country = request.country
        })
    }

}