import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.plugin.allopen") version "1.5.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.5.21"
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    kotlin("plugin.spring") version "1.6.21"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

group = "com.waigel"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.7.3")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.7.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
    testImplementation("org.springframework.security:spring-security-test:5.7.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.3")

    // database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.3")
    implementation("org.postgresql:postgresql:42.2.24")

    // swagger
    implementation("io.swagger.core.v3:swagger-annotations:2.2.4")
    implementation("io.swagger.core.v3:swagger-core:2.2.4")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")

    // tolgee i18n
    implementation("com.waigel.tolgee:tolgee-spring-boot-starter:1.0.2")

    // keycloak
    implementation("org.keycloak:keycloak-spring-boot-starter:19.0.3")
    implementation("com.auth0:java-jwt:4.2.1")

    //utils
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.4.0")

    //pdf
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.300")
}
allOpen {
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict --add-opens java.base/java.time=ALL-UNNAMED")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}