package com.example.towolist.data

enum class ServiceInfo (val packageName: String, val url: String){
    Netflix("com.netflix.mediaclient", "http://www.netflix.com/watch"),
    HBO("com.hbo.hbonow", "https://play.hbomax.com"),
}