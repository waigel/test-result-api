package com.waigel.testresultapi.entities

import com.waigel.testresultapi.models.UserDetailsDTO
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "personal_data")
class PersonalData(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    var firstName: String,
    var lastName: String,
    var birthDate: String,

    var street: String,
    var houseNumber: String,
    var zipcode: String,
    var city: String,
    var country: String,
) : AuditModel() {
    fun copy(): PersonalData {
        return PersonalData(
            id = id,
            firstName = firstName,
            lastName = lastName,
            birthDate = birthDate,
            street = street,
            houseNumber = houseNumber,
            zipcode = zipcode,
            city = city,
            country = country,
        )
    }

    companion object {
        fun fromRequest(userDetailsDTO: UserDetailsDTO): PersonalData {
            return PersonalData(
                firstName = userDetailsDTO.firstName,
                lastName = userDetailsDTO.lastName,
                birthDate = userDetailsDTO.birthDate.toString(),
                street = userDetailsDTO.street,
                houseNumber = userDetailsDTO.houseNumber,
                zipcode = userDetailsDTO.zipcode,
                city = userDetailsDTO.city,
                country = userDetailsDTO.country,
            )
        }
    }
}
