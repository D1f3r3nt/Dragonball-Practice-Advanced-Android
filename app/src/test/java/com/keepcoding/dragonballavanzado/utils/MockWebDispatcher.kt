package com.keepcoding.andoird_class_one.utils

import com.google.common.io.Resources
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File
import java.net.HttpURLConnection

class MockWebDispatcher() : Dispatcher() {
    
    override fun dispatch(request: RecordedRequest): MockResponse {
        
        if (request.path?.contains("locations") == true) {
            return MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(getJson("json/locations.json"))
        }
        
        return MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(getJson("json/heros.json"))
    }

}

internal fun getJson(path: String): String {
    val uri = Resources.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
    
    return ""
}