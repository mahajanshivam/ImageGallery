package com.prosport.android.utils

import com.shivam.android.photofinder.interfaces.ServiceInterface
import org.json.JSONObject
import java.net.URL

class APIController constructor(serviceInjection: ServiceInterface) : ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun get(path: String, params:HashMap<String,String>, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)

    }

    override fun post(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)
    }


}