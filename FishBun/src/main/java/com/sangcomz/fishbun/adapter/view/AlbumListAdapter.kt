package com.sangcomz.fishbun.adapter.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
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
        return ViewHolder(parent, fishton.albumThumbnailSize)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri: Uri = Uri.parse(albumList[position].thumbnailPath)
        fishton.imageAdapter?.loadImage(holder.imgALbumThumb, uri)

        holder.itemView.tag = albumList[position]
        holder.txtAlbumName.text = albumList[position].bucketName
        holder.txtAlbumCount.text = albumList[position].counter.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, PickerActivity::class.java)
            intent.apply {
                putExtra(Define.BUNDLE_NAME.ALBUM.name, albumList[position])
                putExtra(Define.BUNDLE_NAME.POSITION.name, position)
            }
            (it.context as Activity).startActivityForResult(intent, Define().ENTER_ALBUM_REQUEST_CODE)
        }
    }

    override fun getItemCount(): Int = albumList.size

    class ViewHolder(parent: ViewGroup, albumSize: Int) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)) {
        val imgALbumThumb = itemView.img_album_thumb
        val txtAlbumName = itemView.txt_album_name
        val txtAlbumCount = itemView.txt_album_count

        init {
            imgALbumThumb.layoutParams = LinearLayout.LayoutParams(albumSize, albumSize)
        }
    }
}