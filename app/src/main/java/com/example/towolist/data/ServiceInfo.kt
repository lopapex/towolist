package com.example.towolist.data

// Don't forget to add package name to AndroidManifest.xml after new item is added
enum class ServiceInfo (val packageName: String, val url: String, val isWatchNow: Boolean, val faceName: String){
    Netflix("com.netflix.mediaclient", "http://www.netflix.com/watch", true, "Netflix"),
    HBO("com.hbo.hbonow", "https://play.hbomax.com", true, "HBO"),
    Apple("", "https://support.apple.com/en-us/HT201611", false, "Apple"),
    Google("com.android.vending", "https://play.google.com/store/movies", false, "Google"),
    Amazon("com.amazon.avod.thirdpartyclient", "https://www.primevideo.com", true, "Amazon")
}

var services : Map<String, ServiceInfo> = mapOf(
    "Netflix" to ServiceInfo.Netflix,
    "HBO Max" to ServiceInfo.HBO,
    "Apple iTunes" to ServiceInfo.Apple,
    "Google Play Movies" to ServiceInfo.Google,
    "Amazon Prime Video" to ServiceInfo.Amazon,
)