package com.waigel.testresultapi.security

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = [KeycloakSecurityComponents::class])
class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().cors().and().csrf().disable().authorizeRequests()
            .antMatchers("/actuator/*").permitAll() // limit access over ingress controller
            .antMatchers(
                "/swagger-ui.html", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**", "/swagger-ui/**"
            ).permitAll()
            .anyRequest().authenticated()
    }

    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return NullAuthenticatedSessionStrategy()
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    fun keycloakAuthenticationProcessingFilterRegistrationBean(
        filter: KeycloakAuthenticationProcessingFilter
    ): FilterRegistrationBean<*> {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakPreAuthActionsFilterRegistrationBean(
        filter: KeycloakPreAuthActionsFilter
    ): FilterRegistrationBean<*> {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakAuthenticatedActionsFilterBean(
        filter: KeycloakAuthenticatedActionsFilter
    ): FilterRegistrationBean<*> {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakSecurityContextRequestFilterBean(
        filter: KeycloakSecurityContextRequestFilter
    ): FilterRegistrationBean<*> {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    @ConditionalOnMissingBean(
        HttpSessionManager::class
    )
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }

    @Bean
    fun KeycloakConfigResolver(): KeycloakConfigResolver? {
        return KeycloakSpringBootConfigResolver()
    }

    @Configuration
    class WebConfiguration : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600)
        }
    }
}
