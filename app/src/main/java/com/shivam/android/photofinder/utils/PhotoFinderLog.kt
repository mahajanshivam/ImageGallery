package com.shivam.android.photofinder.utils

import android.util.Log
import com.shivam.android.photofinder.BuildConfig

class PhotoFinderLog {


    companion object {

//        private const val flag =true

        private const val TAG = "photofinder_log"

        fun d(msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, msg)
            }
        }
    }
}