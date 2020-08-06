package com.sangcomz.fishbun.ui.picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.sangcomz.fishbun.R
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.picker.listener.OnPickerActionListener
import com.sangcomz.fishbun.ui.picker.model.PickerListItem
import com.sangcomz.fishbun.util.RadioWithTextButton

class PickerAdapter(
    private val imageAdapter: ImageAdapter,
    private val onPickerActionListener: OnPickerActionListener,
    private val hasCameraInPickerPage: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pickerList: List<PickerListItem> = listOf()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return pickerList[position].getItemId()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> {
                ViewHolderCamera(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.header_item, parent, false)
                ).apply {
                    itemView.setOnClickListener {
                        onPickerActionListener.takePicture()
                    }
                }
            }
            else -> {
                ViewHolderImage(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.picker_item, parent, false),
                    imageAdapter,
                    onPickerActionListener
                ).apply {
                    btnThumbCount.setOnClickListener {
                        onPickerActionListener.onClickThumbCount(adapterPosition)
                    }
                    imgThumbImage.setOnClickListener {
                        onPickerActionListener.onClickImage(adapterPosition)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        if (payloads.contains(UPDATE_PAYLOAD)) {
            (holder as? ViewHolderImage)?.update(pickerList[position])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as? ViewHolderImage)?.bindData(pickerList[position])
    }

    override fun getItemCount(): Int {
        return pickerList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && hasCameraInPickerPage) {
            TYPE_CAMERA
        } else {
            super.getItemViewType(position)
        }
    }

    fun setPickerList(pickerList: List<PickerListItem>) {
        this.pickerList = pickerList
        notifyDataSetChanged()
    }

    fun updatePickerListItem(position: Int, image: PickerListItem.Image) {
        this.pickerList = this.pickerList.toMutableList().apply {
            set(position, image)
        }
        notifyItemChanged(position, UPDATE_PAYLOAD)
    }

    fun addImage(path: PickerListItem.Image) {
        val addedIndex = if (hasCameraInPickerPage) 1 else 0

        pickerList.toMutableList()
            .apply { add(addedIndex, path) }
            .also(this::setPickerList)
    }


    class ViewHolderImage(
        itemView: View,
        private val imageAdapter: ImageAdapter,
        private val onPickerActionListener: OnPickerActionListener
    ) :
        RecyclerView.ViewHolder(itemView) {
        val imgThumbImage: ImageView = itemView.findViewById(R.id.img_thumb_image)
        val btnThumbCount: RadioWithTextButton = itemView.findViewById(R.id.btn_thumb_count)

        fun bindData(item: PickerListItem) {
            if (item !is PickerListItem.Image) return

            itemView.tag = item.imageUri
            val viewData = item.viewData

            btnThumbCount.run {
                unselect()
                setCircleColor(viewData.colorActionBar)
                setTextColor(viewData.colorActionBarTitle)
                setStrokeColor(viewData.colorSelectCircleStroke)
            }

            initState(item.selectedIndex, viewData.maxCount == 1)

            imageAdapter.loadImage(imgThumbImage, item.imageUri)
        }

        private fun initState(selectedIndex: Int, isUseDrawable: Boolean) {
            if (selectedIndex != -1) {
                setScale(imgThumbImage, true)
                setRadioButton(isUseDrawable, (selectedIndex + 1).toString())
            } else {
                setScale(imgThumbImage, false)
            }
        }

        fun update(item: PickerListItem) {
            if (item !is PickerListItem.Image) return

            val selectedIndex = item.selectedIndex
            animScale(imgThumbImage, selectedIndex != -1, true)

            if (selectedIndex != -1) {
                setRadioButton(item.viewData.maxCount == 1, (selectedIndex + 1).toString())
            } else {
                btnThumbCount.unselect()
            }
        }

        private fun animScale(view: View, isSelected: Boolean, isAnimation: Boolean) {
            var duration = 200
            if (!isAnimation) duration = 0
            val toScale: Float = if (isSelected) .8f else 1.0f
            ViewCompat.animate(view)
                .setDuration(duration.toLong())
                .scaleX(toScale)
                .scaleY(toScale)
                .withEndAction { if (isAnimation && !isSelected) onPickerActionListener.onDeselect() }
                .start()
        }

        private fun setScale(view: View, isSelected: Boolean) {
            val toScale: Float = if (isSelected) .8f else 1.0f
            view.scaleX = toScale
            view.scaleY = toScale
        }

        private fun setRadioButton(isUseDrawable: Boolean, text: String) {
            if (isUseDrawable) {
                ContextCompat.getDrawable(
                    btnThumbCount.context,
                    R.drawable.ic_done_white_24dp
                )?.let {
                    btnThumbCount.setDrawable(it)
                }
            } else {
                btnThumbCount.setText(text)
            }
        }
    }

    class ViewHolderCamera(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_CAMERA = Int.MIN_VALUE
        private const val UPDATE_PAYLOAD = "payload_update"
    }

}