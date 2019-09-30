package com.sangcomz.fishbun.adapter.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.sangcomz.fishbun.Fishton
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.bean.Album
import com.sangcomz.fishbun.define.Define
import com.sangcomz.fishbun.ui.picker.PickerActivity
import kotlinx.android.synthetic.main.album_item.view.*

class AlbumListAdapter : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {
    private val fishton = Fishton.getInstance()
    var albumList = emptyList<Album>()
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return ViewHolder(view, fishton.albumThumbnailSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri: Uri = Uri.parse(albumList[position].thumbnailPath)
        holder.itemView.img_album_thumb.apply {
            layoutParams = LinearLayout.LayoutParams(holder.albumSize, holder.albumSize)
            fishton.imageAdapter.loadImage(this, uri)
        }

        holder.itemView.setTag(albumList[position])
        val album = holder.itemView.getTag() as Album
        holder.itemView.txt_album_name.text = albumList[position].bucketName
        holder.itemView.txt_album_count.text = album.counter.toString()

        holder.itemView.setOnClickListener {
            val iAlbum = it.getTag() as Album
            val intent = Intent(it.context, PickerActivity::class.java)
            intent.apply {
                putExtra(Define.BUNDLE_NAME.ALBUM.name, iAlbum)
                putExtra(Define.BUNDLE_NAME.POSITION.name, position)
            }
            (it.context as Activity).startActivityForResult(intent, Define().ENTER_ALBUM_REQUEST_CODE)
        }
    }

    override fun getItemCount(): Int = albumList.size

    class ViewHolder(itemView: View, val albumSize: Int) : RecyclerView.ViewHolder(itemView)
}