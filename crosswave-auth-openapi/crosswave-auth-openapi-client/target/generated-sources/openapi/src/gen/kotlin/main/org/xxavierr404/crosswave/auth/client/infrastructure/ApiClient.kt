package org.xxavierr404.crosswave.auth.client.infrastructure;

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.http.ResponseEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.LinkedMultiValueMap
import reactor.core.publisher.Mono

open class ApiClient(protected val client: WebClient) {

    protected inline fun <reified I : Any, reified T: Any?> request(requestConfig: RequestConfig<I>): Mono<ResponseEntity<T>> {
        return prepare(defaults(requestConfig))
            .retrieve()
            .toEntity(object : ParameterizedTypeReference<T>() {})
    }

    protected fun <I : Any> prepare(requestConfig: RequestConfig<I>) =
        client.method(requestConfig)
            .uri(requestConfig)
            .headers(requestConfig)
            .body(requestConfig)

    protected fun <I> defaults(requestConfig: RequestConfig<I>) =
        requestConfig.apply {
            if (body != null && headers[HttpHeaders.CONTENT_TYPE].isNullOrEmpty()) {
                headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
            }
            if (headers[HttpHeaders.ACCEPT].isNullOrEmpty()) {
                headers[HttpHeaders.ACCEPT] = MediaType.APPLICATION_JSON_VALUE
            }
        }

    private fun <I> WebClient.method(requestConfig: RequestConfig<I>)=
        method(HttpMethod.valueOf(requestConfig.method.name))

    private fun <I> WebClient.RequestBodyUriSpec.uri(requestConfig: RequestConfig<I>) =
        uri { builder ->
            builder
                .path(requestConfig.path)
                .queryParams(LinkedMultiValueMap(requestConfig.query))
                .build(requestConfig.params)
        }

    private fun <I> WebClient.RequestBodySpec.headers(requestConfig: RequestConfig<I>) =
        apply { requestConfig.headers.forEach { (name, value) -> header(name, value) } }

    private fun <I : Any> WebClient.RequestBodySpec.body(requestConfig: RequestConfig<I>): WebClient.RequestBodySpec {
        when {
            requestConfig.headers[HttpHeaders.CONTENT_TYPE] == MediaType.MULTIPART_FORM_DATA_VALUE -> {
                val builder = MultipartBodyBuilder()
                (requestConfig.body as Map<String, PartConfig<*>>).forEach { (name, part) ->
                    if (part.body != null) {
                        val partBuilder = builder.part(name, part.body)
                        val partHeaders = part.headers
                        partHeaders.forEach { partBuilder.header(it.key, it.value) }
                    }
                }
                return apply { bodyValue(builder.build()) }
            }
            else -> {
                return apply { if (requestConfig.body != null) bodyValue(requestConfig.body) }
            }
        }
    }
}

inline fun <reified T: Any> parseDateToQueryString(value : T): String {
        /*
        .replace("\"", "") converts the json object string to an actual string for the query parameter.
        The moshi or gson adapter allows a more generic solution instead of trying to use a native
        formatter. It also easily allows to provide a simple way to define a custom date format pattern
        inside a gson/moshi adapter.
        */
        return Serializer.jacksonObjectMapper.writeValueAsString(value).replace("\"", "")
    }
