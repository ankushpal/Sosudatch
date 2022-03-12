package com.example.sosudatech

data class DetailsDataResponse(
    val `data`: ArrayList<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)