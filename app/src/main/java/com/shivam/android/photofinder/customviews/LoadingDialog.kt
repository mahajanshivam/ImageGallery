package com.shivam.android.photofinder.customviews

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import com.shivam.android.photofinder.R

class LoadingDialog(var mContext: Context) {

    var mProgressDialog: SimpleArcDialog? = null

    init {
        this.mProgressDialog = SimpleArcDialog(mContext)
    }


    fun showProgressDialog(mContent: String) {
        mProgressDialog = SimpleArcDialog(mContext)
        val mConfiguration = ArcConfiguration(mContext)
        mConfiguration.text = mContent
        mConfiguration.colors = intArrayOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        mProgressDialog!!.setConfiguration(mConfiguration)
        mProgressDialog!!.show()
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.getLoadingTextView().setGravity(Gravity.CENTER)
    }

    fun showProgressDialog(mContent: String, mCancelable: Boolean) {
        mProgressDialog = SimpleArcDialog(mContext)
        val mConfiguration = ArcConfiguration(mContext)
        mConfiguration.setText(mContent)
        mConfiguration.colors = intArrayOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark))
        mProgressDialog!!.setConfiguration(mConfiguration)
        mProgressDialog!!.show()
        mProgressDialog!!.setCancelable(mCancelable)
        mProgressDialog!!.getLoadingTextView().setGravity(Gravity.CENTER)
    }

    fun destroyDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private inner class LoadingDialogCancelListener : DialogInterface.OnCancelListener {
        override fun onCancel(dialogInterface: DialogInterface) {

        }
    }


}