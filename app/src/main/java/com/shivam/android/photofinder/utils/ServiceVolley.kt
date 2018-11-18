package com.prosport.android.utils

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.shivam.android.photofinder.application.PhotoFinderApplication
import com.shivam.android.photofinder.interfaces.ServiceInterface
import com.shivam.android.photofinder.utils.PhotoFinderLog
import org.json.JSONObject
import java.net.URL


class ServiceVolley : ServiceInterface {
    override fun get(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit) {

//        PhotoFinderLog.d("ServiceVolley get request")
//        var pathUrl = URL(path)

        val jsonObjReq = JsonObjectRequest(Request.Method.GET, path, null,
                Response.Listener { response ->
                    PhotoFinderLog.d("post request OK! Response: $response")
//                    val jsonResponse = JSONObject(response)
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    PhotoFinderLog.d("get request fail! Error: ${error.localizedMessage}")
                    completionHandler(null)
                }
        )

        PhotoFinderLog.d("url path = $path")
        jsonObjReq.setShouldCache(false)
        PhotoFinderApplication.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    val TAG = ServiceVolley::class.java.simpleName
//    val basePath = "https://your/backend/api/"

    override fun post(path: String, params: HashMap<String, String>, completionHandler: (response: JSONObject?) -> Unit) {

        val jsonObjReq = object : StringRequest(Method.POST, path,
                Response.Listener<String> { response ->
                    PhotoFinderLog.d("post request OK! Response: $response")
                    val jsonResponse = JSONObject(response)
                    completionHandler(jsonResponse)
                },
                Response.ErrorListener { error ->
                    PhotoFinderLog.d("post request fail! Error: ${error}")
                    completionHandler(null)
                }) {

            override fun getParams(): MutableMap<String, String> {
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
//                headers.put("Content-Type", "application/json")
                headers.put("Content-Type", "application/x-www-form-urlencoded")
                return headers
            }
        }

        PhotoFinderLog.d("url path = $path")
        jsonObjReq.setShouldCache(false)
        PhotoFinderApplication.instance?.addToRequestQueue(jsonObjReq, TAG)
    }
}