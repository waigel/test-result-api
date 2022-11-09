package com.waigel.testresultapi.entities

import com.waigel.testresultapi.models.UserDetailsDTO
import com.waigel.testresultapi.utils.CryptoHelper
import java.util.UUID
import javax.crypto.SecretKey
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

    companion object {
        fun encryptAndBuildFromRequest(userDetailsDTO: UserDetailsDTO, encryptionKey: SecretKey): PersonalData {
            val personalData = PersonalData(
                firstName = userDetailsDTO.firstName,
                lastName = userDetailsDTO.lastName,
                birthDate = userDetailsDTO.birthDate.toString(),
                street = userDetailsDTO.street,
                houseNumber = userDetailsDTO.houseNumber,
                zipcode = userDetailsDTO.zipcode,
                city = userDetailsDTO.city,
                country = userDetailsDTO.country,
            )
            return CryptoHelper.encryptUserDetails(personalData, encryptionKey)
        }
    }
}
