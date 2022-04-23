package com.example.towolist.data

enum class ServiceInfo (val packageName: String, val url: String){
    Netflix("com.netflix.mediaclient", "http://www.netflix.com/watch"),
    HBO("com.hbo.hbonow", "https://play.hbomax.com"),
    Apple("", "https://support.apple.com/en-us/HT201611"),
    Google("com.android.vending", "https://play.google.com/store/movies"),
    Amazon("com.amazon.avod.thirdpartyclient", "https://www.primevideo.com")
}