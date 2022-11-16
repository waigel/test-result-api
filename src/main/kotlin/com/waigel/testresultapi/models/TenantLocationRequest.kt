package com.waigel.testresultapi.models

class TenantLocationRequest(
    val name: String,
    val street: String,
    val houseNumber: String,
    val zipcode: String,
    val city: String,
    val country: String,
    val logoUrl: String? = null,
) {
}