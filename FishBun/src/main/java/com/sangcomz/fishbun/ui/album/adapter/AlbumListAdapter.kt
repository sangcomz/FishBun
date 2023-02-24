package com.sangcomz.fishbun.ui.album.adapter

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.album.model.Album
import com.sangcomz.fishbun.ui.album.model.AlbumMetaData
import com.sangcomz.fishbun.ui.album.listener.AlbumClickListener
import com.sangcomz.fishbun.util.SquareImageView

class AlbumListAdapter(
    private val albumClickListener: AlbumClickListener,
    private val thumbnailSize: Int,
    private val imageAdapter: ImageAdapter
) :
    RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {

    private var albumList = emptyList<Album>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return albumList[position].id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent,
            thumbnailSize,
            imageAdapter
        ).apply {
            itemView.setOnClickListener {
                albumClickListener.onAlbumClick(adapterPosition, albumList[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(albumList[position])
    }

    fun setAlbumList(albumList: List<Album>) {
        this.albumList = albumList
        notifyDataSetChanged()
    }

    fun updateAlbumMeta(position: Int, addedCount: Int, thumbnailPath: String) {
        val oldAlbum = albumList[position]
        val updateAlbum = oldAlbum.copy(
            metaData = AlbumMetaData(
                oldAlbum.metaData.count + addedCount,
                thumbnailPath
            )
        )
        albumList = albumList.toMutableList().apply { set(position, updateAlbum) }
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int = albumList.size

    class ViewHolder(
        parent: ViewGroup,
        albumSize: Int,
        private val imageAdapter: ImageAdapter
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
    ) {
        private val imgAlbumThumb: SquareImageView = itemView.findViewById(R.id.img_album_thumb)
        private val txtAlbumName: TextView = itemView.findViewById(R.id.txt_album_name)
        private val txtAlbumCount: TextView = itemView.findViewById(R.id.txt_album_count)

        init {
            imgAlbumThumb.layoutParams = LinearLayout.LayoutParams(albumSize, albumSize)
        }

        fun setData(album: Album) {
            val uri: Uri = Uri.parse(album.metaData.thumbnailPath)
            imageAdapter.loadImage(imgAlbumThumb, uri)

            itemView.tag = album
            txtAlbumName.text = album.displayName
            txtAlbumCount.text = album.metaData.count.toString()
        }
    }
}