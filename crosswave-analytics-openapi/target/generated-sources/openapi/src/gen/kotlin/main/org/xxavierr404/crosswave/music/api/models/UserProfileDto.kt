package org.xxavierr404.crosswave.music.api.models

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param id 
 * @param name 
 * @param surname 
 * @param bio 
 * @param likedTracks 
 * @param subscriptions 
 */
data class UserProfileDto(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("surname", required = true) val surname: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("bio", required = true) val bio: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("likedTracks", required = true) val likedTracks: kotlin.collections.List<java.util.UUID>,

    @Schema(example = "null", description = "")
    @get:JsonProperty("subscriptions") val subscriptions: kotlin.collections.List<kotlin.String>? = null
    ) {

}

