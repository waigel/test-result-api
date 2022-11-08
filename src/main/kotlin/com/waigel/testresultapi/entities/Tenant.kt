package com.waigel.testresultapi.entities

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity(name = "tenant")
class Tenant(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),
    @OneToOne(cascade = [javax.persistence.CascadeType.MERGE])
    val location: TenantLocation,
    @ManyToOne
    val company: TenantCompany,
) : AuditModel() {
    companion object {
        fun from(tenantCompany: TenantCompany, tenantLocation: TenantLocation): Tenant {
            return Tenant(
                location = tenantLocation,
                company = tenantCompany,
            )

        }
    }
}
