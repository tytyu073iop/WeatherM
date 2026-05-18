package com.example.weatherm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform