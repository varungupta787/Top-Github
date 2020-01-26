package com.topgithub.demo.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topgithub.demo.R
import com.topgithub.demo.data.network.NetworkUtility
import com.topgithub.demo.dependencyInjection.component.DaggerGithubComponents
import com.topgithub.demo.dependencyInjection.component.GithubComponents
import com.topgithub.demo.models.RepositoryItem
import com.topgithub.demo.ui.adapter.GithubListAdapter
import com.topgithub.demo.ui.interfaces.ItemClickListener
import com.topgithub.demo.viewmodel.GithubViewModel
import com.topgithub.demo.viewmodel.ViewModelFactory
import javax.inject.Inject


class RepositoryListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GithubViewModel
    private lateinit var mToolbar: Toolbar
    private lateinit var githubRecyclerView: RecyclerView
    private lateinit var noInternetScreen: LinearLayout
    private lateinit var tryAgainButton: Button
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var githubAdapter: GithubListAdapter
    private var mProgressLoader: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.topgithub.demo.R.layout.repository_list)
        requestPermission()
        injectDependency()
        initViews()
        setToolbar()
        setViews()
        initData()
        setListeners()
    }

    private fun setListeners() {
        tryAgainButton.setOnClickListener(View.OnClickListener {
            hideNoInternetScreen()
            getRepositoryListData()
        })
    }

    private fun injectDependency() {

        var components: GithubComponents = DaggerGithubComponents.builder().build();
        components.injectRepositoryListActivity(this)
        viewModel =
            ViewModelProviders
                .of(this, viewModelFactory)
                .get(GithubViewModel::class.java)
    }

    private fun setToolbar() {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(com.topgithub.demo.R.drawable.toolbar_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        mToolbar.setTitle(getString(R.string.app_title))
    }

    private fun initViews() {
        mToolbar = findViewById(com.topgithub.demo.R.id.toolbar) as Toolbar
        githubRecyclerView =
            findViewById(com.topgithub.demo.R.id.github_recyclerview) as RecyclerView
        noInternetScreen = findViewById(com.topgithub.demo.R.id.no_internet_screen) as LinearLayout
        tryAgainButton = findViewById(com.topgithub.demo.R.id.try_again_botton) as Button
        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        githubRecyclerView.setHasFixedSize(true)
        githubRecyclerView.layoutManager = mLayoutManager
        githubAdapter = GithubListAdapter(
            this, onItemClickListener
        )
        githubRecyclerView.adapter = githubAdapter
    }

    val onItemClickListener = object : ItemClickListener {
        override fun onItemClick(item: RepositoryItem, view: View) {
            startRepoDetailActivity(item, view)
        }
    }

    private fun startRepoDetailActivity(item: RepositoryItem, view: View) {
        val intent = Intent(
            this@RepositoryListActivity,
            RepositoryDetailActivity::class.java
        )
        intent.putExtra("data", item)

        val activityOptionsCompat =
            makeSceneTransitionAnimation(
                this@RepositoryListActivity,
                view, getString(R.string.transition_element_name)
            )
        ActivityCompat.startActivity(
            this@RepositoryListActivity,
            intent, activityOptionsCompat.toBundle()
        )
    }

    private fun setViews() {
        githubRecyclerView.visibility = View.VISIBLE
        noInternetScreen.visibility = View.GONE
    }

    private fun initData() {
        getRepositoryListData()
    }

    private fun getRepositoryListData() {
        if (!NetworkUtility.isNetworkAvailable) {
            showNoInternetScreen()
            return
        }
        viewModel.getRepositoryListData()
        viewModel.githubRepositoryList.observe(this, Observer { repoList ->

            githubAdapter.setData(repoList)
            githubAdapter.notifyDataSetChanged()

        })
        viewModel.githubRepositoryError.observe(this, Observer { error ->
        })

        viewModel.loadingState.observe(this, Observer { isLoading ->
            if (isLoading) {
                showLoader()
            } else {
                hideLoader()
            }
        })
    }

    fun showNoInternetScreen() {
        noInternetScreen.visibility = View.VISIBLE
        githubRecyclerView.setVisibility(View.GONE)
    }

    private fun hideNoInternetScreen() {
        githubRecyclerView.setVisibility(View.VISIBLE)
        noInternetScreen.visibility = View.GONE
    }

    fun showLoader() {
        if (mProgressLoader == null) {
            mProgressLoader = ProgressDialog(this)
        }
        mProgressLoader!!.setMessage(getString(R.string.progress_title_list))
        mProgressLoader!!.setTitle(getString(R.string.progress_please_wait))
        mProgressLoader!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressLoader!!.setCancelable(false)
        mProgressLoader!!.show()
    }

    fun hideLoader() {
        if (mProgressLoader != null) {
            mProgressLoader!!.dismiss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    this@RepositoryListActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(
                    this@RepositoryListActivity,
                    getString(R.string.permissin_granted),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // Permission Denied
                Toast.makeText(this@RepositoryListActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

