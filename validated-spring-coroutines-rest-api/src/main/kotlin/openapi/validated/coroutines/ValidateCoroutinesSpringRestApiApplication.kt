package com.toda.openapi.validated.coroutines

import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.StandardReflectionParameterNameDiscoverer
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class ValidatedCoroutinesSpringRestApiApplication

fun main(args: Array<String>) {
    runApplication<ValidatedCoroutinesSpringRestApiApplication>(*args)
}

@Configuration
class ValidationConfig {

    /**
     * @see https://github.com/spring-projects/spring-framework/issues/23152
     * Replaced default [org.springframework.core.KotlinReflectionParameterNameDiscoverer]
     */
    @Bean
    @Primary
    fun validator() = LocalValidatorFactoryBean().apply {
        setParameterNameDiscoverer(StandardReflectionParameterNameDiscoverer())
    }
}

/**
 * Spring managed bean for Open API additional API Information and Security documentation.
 */
@OpenAPIDefinition(info = Info(
        title = " OpenAPI definition for Servlet Spring example",
        version = "v1",
        description = "Super cool API for vacation management. Enjoy!",
        license = License(name = "Apache license", url = "https://www.apache.org/licenses/LICENSE-2.0")
))
// TODO causing problem on swagger-ui - flows
@SecuritySchemes(
        SecurityScheme(name = "Standard OAuth", type = SecuritySchemeType.OAUTH2, flows = OAuthFlows(implicit = OAuthFlow())),
        SecurityScheme(name = "Custom security layer - super secured key", type = SecuritySchemeType.APIKEY)
)
// @SecurityScheme(name = "Standard oAuth", type = SecuritySchemeType.OAUTH2) //repeatable annotation not yet available
@Component
class OpenApiDescription

@RestController
@RequestMapping("/api/employees/{employeeId}/vacations")
@Validated
@Tag(name = "employee", description = "Everything employee related.")
class EmployeeController(private val vacationService: VacationService) {

    @GetMapping
    @Operation(summary = "Employee vacations list resource")
    suspend fun getEmployeeVacationList(@PathVariable
                                        @Min(value = 1, message = "id must be greater than or equal to 1")
                                        employeeId: EmployeeId,
                                        @RequestParam(required = false, defaultValue = "false")
                                        // most of the info (name, in, required, type) retrieved from Spring/declaration
                                        @Parameter(description = "specified if output should be expanded (true/false)")
                                        expanded: Boolean = false): EmployeeVacationListDto =
            vacationService.getEmployeeVacationList(employeeId)

    @PostMapping
    @Operation(summary = "Employee vacation set resource",
            responses = [
                ApiResponse(responseCode = "201", headers = [Header(name = "Location")],
                        content = [Content(mediaType = "application/json")])
            ])
    suspend fun addEmployeeVacation(@PathVariable employeeId: EmployeeId,
                                    @Validated @RequestBody vacation: VacationDto,
                                    serverHttpRequest: ServerHttpRequest
    ): ResponseEntity<Unit> {
        val vacationId = vacationService.addVacationToEmployee(employeeId, vacation)
        val locationUri = UriComponentsBuilder.fromUri(serverHttpRequest.uri).pathSegment("{vacationId}")
        return ResponseEntity.created(locationUri.build(vacationId)).body(Unit)
    }
}

@RestController
@RequestMapping("/api/vacations")
class VacationController {

    @GetMapping("/{vacationType}")
    suspend fun getVacationDescription(@PathVariable vacationType: VacationType) = vacationType

}

typealias EmployeeId = Long
typealias VacationId = Long

@JsonRootName("employee")
data class Employee(val id: EmployeeId, @Email val email: String)

@JsonRootName("vacationEntry")
data class EmployeeVacationListDto(
        val employee: Employee,
        val vacations: List<VacationDto>)

@JsonRootName("vacation")
@JsonTypeName("vacation")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
data class VacationDto(
        val type: VacationType,
        @field:FutureOrPresent
        val from: LocalDate,
        @field:FutureOrPresent
        val to: LocalDate,
        @field:NotEmpty
        val messageToManager: String,
        val assignedManager: EmployeeId
)

enum class VacationType(val description: String) {
    PTO("paid time off"),
    WFH("work from home"),
    CTO("compensatory time off"),
    WFHM("mandatory work from home a.k.a. COVID-19 WFH");
}

interface VacationService {
    fun getEmployeeVacationList(employeeId: EmployeeId): EmployeeVacationListDto
    fun addVacationToEmployee(employeeId: EmployeeId, validation: VacationDto): VacationId
}

@Service
class VacationServiceInMemory : VacationService {

    val employeeVacationStorage: Map<EmployeeId, EmployeeVacationListDto>

    init {
        val vacationDto = VacationDto(
                VacationType.PTO,
                LocalDate.of(2018, 2, 3),
                LocalDate.of(2018, 2, 5),
                "Going to vacation with family - Michigan",
                1021
        )

        employeeVacationStorage = mapOf(1L to EmployeeVacationListDto(
                Employee(1, "fredrick.mahoon@developers.company.com"),
                listOf(vacationDto)
        ))
    }

    override fun getEmployeeVacationList(employeeId: EmployeeId): EmployeeVacationListDto =
            employeeVacationStorage[employeeId]
                    ?: throw IllegalArgumentException("No entry for the employee: $employeeId")

    override fun addVacationToEmployee(employeeId: EmployeeId, validation: VacationDto): VacationId {
        logger.info { "adding vacation entry for employee $employeeId" }
        logger.info { validation }
        return 5
    }
}
