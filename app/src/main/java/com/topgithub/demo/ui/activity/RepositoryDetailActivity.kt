package com.topgithub.demo.ui.activity

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.webkit.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.topgithub.demo.R
import com.topgithub.demo.TopGithubApplication
import com.topgithub.demo.data.network.NetworkUtility
import com.topgithub.demo.models.RepositoryItem


class RepositoryDetailActivity : AppCompatActivity() {

    private lateinit var repoData: RepositoryItem
    private lateinit var mToolbar: Toolbar
    /* private lateinit var noInternetScreen: LinearLayout
     private lateinit var tryAgainButton: Button*/
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var repoName: TextView
    private lateinit var repoDescription: TextView
    private lateinit var githWebview: WebView
    private var mProgressLoader: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_detail)
        chechInternetConnection()
        initViews()
        setToolbar()
        getIntentData()
        setData()
        loadWebView()
    }

    private fun chechInternetConnection() {
        if (!NetworkUtility.isNetworkAvailable) {
            Toast.makeText(this, getString(R.string.no_internet_msg), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun getIntentData() {
        repoData = intent.getParcelableExtra<RepositoryItem>("data")
        if (repoData == null) {
            Toast.makeText(this, getString(R.string.somthing_wrong_msg), Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun setToolbar() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(R.drawable.toolbar_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun initViews() {
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        userImage = findViewById(R.id.user_image) as ImageView
        userName = findViewById(R.id.user_name) as TextView
        repoName = findViewById(R.id.repo_name) as TextView
        repoDescription = findViewById(R.id.repo_description) as TextView
        githWebview = findViewById(R.id.wv_repo)
    }

    private fun setData() {
        mToolbar.setTitle(repoData.name)
        userName.setText(getString(R.string.user) + " "+ repoData.username)
        repoName.setText(getString(R.string.repo)+ " " + repoData.repo.name)
        repoDescription.setText(getString(R.string.description) + " "+ repoData.repo.description)
        TopGithubApplication.getImageLoader().DisplayImage(repoData.avatar, userImage)
    }

    private fun loadWebView() {
        githWebview.getSettings().setJavaScriptEnabled(true)
        githWebview.getSettings().setDomStorageEnabled(true)
        githWebview.getSettings().setLoadsImagesAutomatically(true)
        githWebview.getSettings().setBuiltInZoomControls(true)
        githWebview.setWebChromeClient(WebChromeClient())
        githWebview.setWebViewClient(WebViewClients())
        githWebview.loadUrl(repoData.repo.url)
    }

    inner class WebViewClients : WebViewClient() {
        init {
            showLoader()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            hideLoader()
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            hideLoader()
            Toast.makeText(
                this@RepositoryDetailActivity,
                getString(R.string.loading_error), Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun showLoader() {
        if (mProgressLoader == null) {
            mProgressLoader = ProgressDialog(this)
        }
        mProgressLoader!!.setMessage(getString(R.string.progree_title_detail))
        mProgressLoader!!.setTitle(getString(R.string.progress_please_wait))
        mProgressLoader!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressLoader!!.setCancelable(false)
        mProgressLoader!!.show()
    }

    private fun hideLoader() {
        if (mProgressLoader != null) {
            mProgressLoader!!.dismiss()
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this)
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
