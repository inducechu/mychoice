package com.induce.universityservice.mapper

import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.model.University

fun University.toResponse() = UniversityResponse(
    id = id!!,
    code = code,
    name = name,
    cityId = city.id!!,
    cityName = city.name,
    description = description
)
