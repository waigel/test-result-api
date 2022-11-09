package com.waigel.testresultapi.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.waigel.testresultapi.models.CreateTenantRequestDTO
import com.waigel.testresultapi.models.TenantLocationRequest
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity(name = "tenant_location")
class TenantLocation(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    var name: String,
    var street: String,
    var houseNumber: String,
    var zipcode: String,
    var city: String,
    var country: String,

    val logoUrl: String? = null,

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    val tenant: Tenant? = null

    ) : AuditModel() {

    companion object {
        fun from(createTenantRequestDTO: CreateTenantRequestDTO): TenantLocation {
            return TenantLocation(
                name = createTenantRequestDTO.name,
                street = createTenantRequestDTO.street,
                houseNumber = createTenantRequestDTO.houseNumber,
                zipcode = createTenantRequestDTO.zipcode,
                city = createTenantRequestDTO.city,
                country = createTenantRequestDTO.country,
                logoUrl = createTenantRequestDTO.logoUrl,
            )

        }

        fun from(createTenantRequestDTO: TenantLocationRequest): TenantLocation {
            return TenantLocation(
                name = createTenantRequestDTO.name,
                street = createTenantRequestDTO.street,
                houseNumber = createTenantRequestDTO.houseNumber,
                zipcode = createTenantRequestDTO.zipcode,
                city = createTenantRequestDTO.city,
                country = createTenantRequestDTO.country,
            )
        }
    }
}