package com.toda.openapi.validatedSpringRestApi.fuel

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.awaitResponseResult
import com.github.kittinunf.fuel.core.deserializers.ByteArrayDeserializer
import com.github.kittinunf.fuel.httpGet
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.URLProtocol
import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class FuelClient {

    @EventListener(ApplicationReadyEvent::class)
    fun callApiKtor() = runBlocking {
        val client = HttpClient(Apache) {
            install(Logging) {
                level = LogLevel.HEADERS
            }
        }

        // details: https://ktor.io/clients/http-client/quick-start/responses.html
        val response = client.request<HttpResponse> {
            url {
                protocol = URLProtocol.HTTPS
                host = "secure.gooddata.com"
                path("gdc")
            }
            headers {
                append(Headers.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            }
        }

        println("Ktor")
        println("status: ${response.status}")
        println("body: ${response.readText()}")
    }

    @EventListener(ApplicationReadyEvent::class)
    fun callApi() {
        "https://secure.gooddata.com/gdc"
                .httpGet()
                .header(Headers.ACCEPT to MediaType.APPLICATION_JSON)
                .response { request, response, result ->
                    println(request)
                    println(response)
                    println("Status code: ${response.statusCode}")
                    val (bytes, _) = result
                    println("[response bytes] ${String(bytes!!)}")
                }
    }

    @EventListener(ApplicationReadyEvent::class)
    fun callApiCoroutines() = runBlocking {
        "https://secure.gooddata.com/gdc"
                .httpGet()
                .header(Headers.ACCEPT to MediaType.APPLICATION_JSON)
                .awaitResponseResult(ByteArrayDeserializer())
                .third.component1()
                .let {
                    println("Response from coroutine based impl:")
                    println("[response bytes] ${String(it!!)}")
                }
    }

    @EventListener(ApplicationReadyEvent::class)
    fun callGetApiLachtan() = call(Method.GET, "https://secure.gooddata.com/gdc")

    @EventListener(ApplicationReadyEvent::class)
    fun callPutApiLachtan() = call(Method.PUT, "https://jsonplaceholder.typicode.com/posts/1",
            """
                        {
                          "id": 1,
                          "title": "adasda",
                          "body": "adasdasdasd",
                          "userId": 1
                        }
                    """
    )

    fun call(method: Method, url: String, data: String? = null) {
        val request = Fuel.request(method, url)
        request
                .appendHeader(Headers.ACCEPT to MediaType.APPLICATION_JSON)
                .appendHeader(Headers.CONTENT_TYPE to MediaType.APPLICATION_JSON)
        data?.let {
            println("sending body: $it")
            request.body(it)
        }
        measureTimeMillis {
            val (_, response, _) = request.responseString()
            println("lachtan request - $method")
            println(response.statusCode)
            println(String(response.data))
        }.let { println("Took ${it}ms") }
    }
}