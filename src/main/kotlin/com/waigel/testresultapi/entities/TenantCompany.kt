package com.waigel.testresultapi.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.waigel.testresultapi.models.TenantCompanyRequestDTO
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

/**
 * The TenantCompany entity.
 */
@Entity(name = "tenant_company")
class TenantCompany(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    var name: String,
    var street: String,
    var houseNumber: String,
    var zipcode: String,
    var city: String,
    var country: String,

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    val tenants: List<Tenant> = listOf()

    ) : AuditModel() {


    companion object {
        fun from(request: TenantCompanyRequestDTO): TenantCompany {
            return TenantCompany(
                name = request.name,
                street = request.street,
                houseNumber = request.houseNumber,
                zipcode = request.zipcode,
                city = request.city,
                country = request.country,
            )
        }
    }
}