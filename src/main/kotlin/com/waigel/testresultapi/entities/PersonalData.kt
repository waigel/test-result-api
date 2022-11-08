package com.waigel.testresultapi.entities

import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "personal_data")
class PersonalData(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,

    val street: String,
    val houseNumber: String,
    val zipcode: String,
    val city: String,
    val country: String,
) : AuditModel() {
}
