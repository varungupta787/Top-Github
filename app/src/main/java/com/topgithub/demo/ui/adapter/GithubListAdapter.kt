package com.topgithub.demo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.topgithub.demo.R
import com.topgithub.demo.TopGithubApplication
import com.topgithub.demo.cache.ImageLoader
import com.topgithub.demo.models.RepositoryItem
import com.topgithub.demo.ui.interfaces.ItemClickListener

class GithubListAdapter(
    private var mContext: Context, private var listener: ItemClickListener
) : RecyclerView.Adapter<GithubListAdapter.GithubViewHolder>() {

    private var repoList: List<RepositoryItem> = ArrayList<RepositoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubViewHolder {
        val mInflater = LayoutInflater.from(mContext)
        val view = mInflater.inflate(R.layout.repo_item, parent, false)
        return GithubViewHolder(view)
    }

    override fun onBindViewHolder(holder: GithubViewHolder, position: Int) {
        holder.itemView.setTag(position)
        holder.itemView.setOnClickListener(itemClickListener)
        val repoItem = repoList.get(position)
        holder.name.setText(mContext.getString(R.string.author) + " "+ repoItem.name.toUpperCase())
        holder.userName.setText(mContext.getString(R.string.user) + " "+repoItem.username)
        holder.repoName.setText(mContext.getString(R.string.repo) + " "+ repoItem.repo.name)
        TopGithubApplication.getImageLoader().DisplayImage(repoItem.avatar, holder.userImage)
    }

    private var itemClickListener = View.OnClickListener { view ->
        var position = view.getTag() as Int
        listener.onItemClick(repoList.get(position), view)
    }

    override fun getItemCount(): Int {
        return repoList?.size ?: 0
    }

    fun setData(repoList: List<RepositoryItem>) {
        this.repoList = repoList
    }

    inner class GithubViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var userImage: ImageView
        var name: TextView
        var userName: TextView
        var repoName: TextView

        init {
            userImage = view.findViewById(R.id.user_image) as ImageView
            name = view.findViewById(R.id.name) as TextView
            userName = view.findViewById(R.id.user_name) as TextView
            repoName = view.findViewById(R.id.repo_name) as TextView
        }
    }

}
