package com.waigel.testresultapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync

@OpenAPIDefinition(
    info = Info(
        title = "Test Result API",
        version = "1.0",
        description = "The REST API for the Test Result corona management system.",
        contact = Contact(url = "https://testperfect.de", name = "Novax Digital GmbH", email = "info@testperfect.de")
    )
)
@SecurityScheme(
    name = "oauth2",
    type = SecuritySchemeType.OAUTH2,
    flows = OAuthFlows(
        implicit = OAuthFlow(
            authorizationUrl = "https://auth.testperfect.dev/realms/testperfect/protocol/openid-connect/auth",

            )
    )
)
@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
@EntityScan
@EnableJpaRepositories
class TestResultApiApplication

fun main(args: Array<String>) {
    runApplication<TestResultApiApplication>(*args)
}
