package com.example.towolist.webservice.response

import com.squareup.moshi.Json

data class WatchProviderListResponse (
    val id: Int,
    val results: WatchProviderResultResponse
)

data class WatchProviderResultResponse (
    val AR : WatchProviderByStateResponse,
    val AT : WatchProviderByStateResponse,
    val AU : WatchProviderByStateResponse,
    val BE : WatchProviderByStateResponse,
    val BR : WatchProviderByStateResponse,
    val CA : WatchProviderByStateResponse,
    val CH : WatchProviderByStateResponse,
    val CL : WatchProviderByStateResponse,
    val CO : WatchProviderByStateResponse,
    val CZ : WatchProviderByStateResponse,
    val DE : WatchProviderByStateResponse,
    val DK : WatchProviderByStateResponse,
    val EC : WatchProviderByStateResponse,
    val ES : WatchProviderByStateResponse,
    val FI : WatchProviderByStateResponse,
    val FR : WatchProviderByStateResponse,
    val GB : WatchProviderByStateResponse,
    val HU : WatchProviderByStateResponse,
    val IE : WatchProviderByStateResponse,
    val IN : WatchProviderByStateResponse,
    val IT : WatchProviderByStateResponse,
    val JP : WatchProviderByStateResponse,
    val MX : WatchProviderByStateResponse,
    val NL : WatchProviderByStateResponse,
    val NO : WatchProviderByStateResponse,
    val NZ : WatchProviderByStateResponse,
    val PE : WatchProviderByStateResponse,
    val PL : WatchProviderByStateResponse,
    val PT : WatchProviderByStateResponse,
    val RO : WatchProviderByStateResponse,
    val RU : WatchProviderByStateResponse,
    val SE : WatchProviderByStateResponse,
    val TR : WatchProviderByStateResponse,
    val US : WatchProviderByStateResponse,
    val VE : WatchProviderByStateResponse,
    val ZA : WatchProviderByStateResponse
)

data class WatchProviderByStateResponse(
    val link : String,
    val buy : List<WatchProviderInfoResponse>?,
    val flatrate : List<WatchProviderInfoResponse>?
)

data class WatchProviderInfoResponse(
    @field:Json(name = "display_priority")
    val displayPriority : Int,
    @field:Json(name = "logo_path")
    val logoPath : String,
    @field:Json(name = "provider_id")
    val providerId : Int,
    @field:Json(name = "provider_name")
    val providerName : String
)