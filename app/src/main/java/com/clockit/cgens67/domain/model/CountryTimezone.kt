package com.clockit.cgens67.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CountryTimezone(
    val countryName: String,
    val countryCode: String,
    val zoneId: String,
    val zoneName: String
)
