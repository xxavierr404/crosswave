/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.xxavierr404.crosswave.auth.client.models


import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 
 *
 * @param token JWT-токен авторизации
 */


data class LoginRequestDto (

    /* JWT-токен авторизации */
    @get:JsonProperty("token")
    val token: kotlin.String

) {


}

