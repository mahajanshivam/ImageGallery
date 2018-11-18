package com.shivam.android.photofinder.activity

import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shivam.android.photofinder.R
import com.shivam.android.photofinder.adapter.ImageGridRecyclerAdapter
import com.shivam.android.photofinder.adapter.SearchListAdapter
import com.shivam.android.photofinder.customviews.CustomMaterialSearchView
import com.shivam.android.photofinder.customviews.LoadingDialog
import com.shivam.android.photofinder.utils.Constants
import com.shivam.android.photofinder.utils.PhotoFinderLog
import com.shivam.android.photofinder.utils.TinyDB
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    private var imagesUrlList: ArrayList<String> = ArrayList()
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var imageGridRecyclerAdapter: ImageGridRecyclerAdapter

    private var loading = true
    private var page = 1
    private var pastVisiblesItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var urlString: String = ""
    private var queryString: String = ""
    private var startIndex = 1
    private lateinit var loadingDialog: LoadingDialog
    private var searchItems: ArrayList<String> = ArrayList()

    lateinit var tinydb: TinyDB
    lateinit var popupWindow: PopupWindow
    lateinit var searchRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tinydb = TinyDB(this@MainActivity)

        loadingDialog = LoadingDialog(this@MainActivity)
        initActionBar()

        PhotoFinderLog.d("main activity onCreate")

        gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
        imageGridRecyclerAdapter = ImageGridRecyclerAdapter(this@MainActivity, imagesUrlList)
        photosRecyclerView.layoutManager = gridLayoutManager
        photosRecyclerView.adapter = imageGridRecyclerAdapter



        photosRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false
                            PhotoFinderLog.d("photosRecyclerView onScrolled now calling next ")
                            //Do pagination.. i.e. fetch new data

                            page++
                            startIndex = (10 * page) + 1
                            urlString = "https://www.googleapis.com/customsearch/v1?q=$queryString&key=${resources.getString(R.string.key_google_api)}&cx=${Constants.cx}&searchType=image&alt=json&start=$startIndex"
                            searchImageHit(1)
                        }
                    }
                }
            }
        })

    }

    private fun initActionBar() {
//        setSupportActionBar(toolbar)
        supportActionBar!!.title = resources.getString(R.string.app_name)
    }

    private fun initSearchRecycler() {
        searchItems = tinydb.getListString("search_items")

        val layout = layoutInflater.inflate(R.layout.item_search_list, findViewById<FrameLayout>(R.id.fl_root))

        // Creating the PopupWindow
        popupWindow = PopupWindow(this@MainActivity)
        popupWindow.contentView = layout
        popupWindow.width = LinearLayout.LayoutParams.MATCH_PARENT
        popupWindow.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow.isFocusable = true
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(BitmapDrawable())

//        popupWindow.showAtLocation(photosRecyclerView,Gravity.TOP,0,0)
        popupWindow.showAsDropDown(view)

        searchRecyclerView = layout.findViewById<RecyclerView?>(R.id.searchRecyclerView)!!
        searchRecyclerView.adapter = SearchListAdapter(this@MainActivity, searchItems)
        searchRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_actionbar_menu, menu)
        this.menu = menu!!

        searchButtonCLicked()

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.action_search -> {
                PhotoFinderLog.d("onOptionsItemSelected action_search clicked")
//                onSearchRequested()

            }

            R.id.action_2by2 -> {
                gridLayoutManager.spanCount = 2

            }

            R.id.action_3by3 -> {
                gridLayoutManager.spanCount = 3

            }

            R.id.action_4by4 -> {
                gridLayoutManager.spanCount = 4

            }

        }

        return true
    }

    private fun searchButtonCLicked() {
        var manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var search = menu.findItem(R.id.action_search).actionView as SearchView

        search.setSearchableInfo(manager.getSearchableInfo(componentName))

        search.setOnQueryTextListener(object : CustomMaterialSearchView.OnQueryTextListener, SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                queryString = query!!.trim().replace(" ", "+")
                urlString = "https://www.googleapis.com/customsearch/v1?q=$queryString&key=${resources.getString(R.string.key_google_api)}&cx=${Constants.cx}&searchType=image&alt=json"
//                var urlString = "https://www.googleapis.com/customsearch/v1"

                searchItems.add(query)
                tinydb.putListString("search_items", searchItems)

                searchImageHit(0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

//
//                if (newText!!.length == 1) {
//                    initSearchRecycler()
//
//
//                    loadHistory(newText)
//                }
                return true
            }

        })

    }


    private fun loadHistory(query: String?) {

        searchItems = tinydb.getListString("search_items")


    }


    private fun searchImageHit(case: Int) {                  // 0 for first hit and 1 for pagination

        PhotoFinderLog.d("searchImageHit starting = $urlString")

        if (case == 0) {
            imagesUrlList.clear()
        }

//        progress_bar.visibility = View.VISIBLE

        loadingDialog.showProgressDialog("Fetching Images")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, urlString, null,
                Response.Listener { response ->
                    loadingDialog.destroyDialog()

                    PhotoFinderLog.d("searchImageHit response = " + response.toString())

                    var itemsArray = response.getJSONArray("items")

                    for (i in 0 until itemsArray.length()) {
                        var itemobject = itemsArray.getJSONObject(i)
                        var imageObject = itemobject.getJSONObject("image");
                        var imageLink = imageObject.getString("thumbnailLink")

//                        PhotoFinderLog.d("searchImageHit response image link = $imageLink")

                        imagesUrlList.add(imageLink)
                    }
                    PhotoFinderLog.d("searchImageHit imagesUrlList size = " + imagesUrlList.size)

                    photosRecyclerView.visibility = View.VISIBLE
                    ll_press_search.visibility = View.GONE
                    imageGridRecyclerAdapter.notifyDataSetChanged()

                    if (page <= 9) {
                        loading = true
                    }
                },
                Response.ErrorListener { error ->
                    loadingDialog.destroyDialog()
                    PhotoFinderLog.d("searchImageHit error = " + error.toString())
                    Toast.makeText(this@MainActivity,"Something went wrong...Please try again later",Toast.LENGTH_SHORT).show()

                }
        )
        var queue = Volley.newRequestQueue(this@MainActivity)
        queue.add(jsonObjectRequest)

    }


}
