package com.waigel.testresultapi

class Constants {
    companion object {
        const val API_VERSION = "v1"
        const val API_BASE_PATH = "/api/$API_VERSION"

        const val API_TENANT_COMPANY_CONTROLLER_PATH = "$API_BASE_PATH/tenant-company"
        const val API_TENANT_CONTROLLER_PATH = "$API_BASE_PATH/company/{companyId}/tenants"

        const val API_TCA_BASE_PATH = "$API_BASE_PATH/tca"
        const val API_TCA_SUBMIT_TEST_RESULT_PATH = "$API_TCA_BASE_PATH/submit-test-result"

        const val API_PUBLIC_DECRYPT_DATA_PATH = "$API_BASE_PATH/public/decrypt-data"

        const val TENANT_HEADER = "X-Tenant"
        const val ACCESS_TOKEN_HEADER = "X-Access-Token"
        const val CAPTCHA_TOKEN_HEADER = "X-Captcha-Token"
    }
}