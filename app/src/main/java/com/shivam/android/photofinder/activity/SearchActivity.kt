package com.shivam.android.photofinder.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.shivam.android.photofinder.R
import com.shivam.android.photofinder.utils.PhotoFinderLog
import kotlinx.android.synthetic.main.activity_search.*
import android.widget.Toast
import android.content.Intent
import com.shivam.android.photofinder.utils.StoresData
import android.app.SearchManager
import com.shivam.android.photofinder.adapter.CustomSearchAdapter


class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                PhotoFinderLog.d("SearchActivity onItemClickListener ")

                handleSearch()
            }

        }

    }

    private fun handleSearch() {
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val searchQuery = intent.getStringExtra(SearchManager.QUERY)

            PhotoFinderLog.d("onOptionsItemSelected action_search clicked searchQuery = $searchQuery")


            val adapter = CustomSearchAdapter(this,
                    android.R.layout.simple_dropdown_item_1line,
                    StoresData.filterData(searchQuery))
            listView.adapter = adapter

        } else if (Intent.ACTION_VIEW == intent.action) {
            val selectedSuggestionRowId = intent.dataString
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item
            Toast.makeText(this, "selected search suggestion " + selectedSuggestionRowId!!,
                    Toast.LENGTH_SHORT).show()
        }
    }
}
