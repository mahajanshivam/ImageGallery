package com.shivam.android.photofinder.interfaces

import org.json.JSONObject
import java.net.URL

interface ServiceInterface {

    fun post(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit)

    fun get(path: String, params:HashMap<String,String>, completionHandler: (response: JSONObject?) -> Unit)

}