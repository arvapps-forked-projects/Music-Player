package com.simplemobiletools.musicplayer.adapters

import android.content.ContentUris
import android.net.Uri
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.simplemobiletools.commons.adapters.MyRecyclerViewAdapter
import com.simplemobiletools.commons.extensions.getColoredDrawableWithColor
import com.simplemobiletools.commons.views.MyRecyclerView
import com.simplemobiletools.musicplayer.R
import com.simplemobiletools.musicplayer.activities.SimpleActivity
import com.simplemobiletools.musicplayer.models.Artist
import kotlinx.android.synthetic.main.item_artist.view.*
import java.util.*

class ArtistsAdapter(activity: SimpleActivity, val artists: ArrayList<Artist>, recyclerView: MyRecyclerView, itemClick: (Any) -> Unit) :
        MyRecyclerViewAdapter(activity, recyclerView, null, itemClick) {
    private val placeholder = resources.getColoredDrawableWithColor(R.drawable.ic_headset_padded, textColor)

    init {
        setupDragListener(true)
    }

    override fun getActionMenuId() = R.menu.cab_artists

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createViewHolder(R.layout.item_artist, parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artists.getOrNull(position) ?: return
        holder.bindView(artist, true, true) { itemView, layoutPosition ->
            setupView(itemView, artist)
        }
        bindViewHolder(holder)
    }

    override fun getItemCount() = artists.size

    override fun prepareActionMode(menu: Menu) {}

    override fun actionItemPressed(id: Int) {}

    override fun getSelectableItemCount() = artists.size

    override fun getIsItemSelectable(position: Int) = true

    override fun getItemSelectionKey(position: Int) = artists.getOrNull(position)?.id

    override fun getItemKeyPosition(key: Int) = artists.indexOfFirst { it.id == key }

    override fun onActionModeCreated() {}

    override fun onActionModeDestroyed() {}

    private fun getItemWithKey(key: Int): Artist? = artists.firstOrNull { it.id == key }

    private fun setupView(view: View, artist: Artist) {
        view.apply {
            artist_frame?.isSelected = selectedKeys.contains(artist.id)
            artist_title.text = artist.title
            artist_title.setTextColor(textColor)

            val albums = resources.getQuantityString(R.plurals.albums, artist.albumCnt, artist.albumCnt)
            val tracks = resources.getQuantityString(R.plurals.tracks, artist.trackCnt, artist.trackCnt)
            artist_albums_tracks.text = "$albums, $tracks"
            artist_albums_tracks.setTextColor(textColor)

            val artworkUri = Uri.parse("content://media/external/audio/albumart")
            val albumArtUri = ContentUris.withAppendedId(artworkUri, artist.albumArtId)

            val options = RequestOptions()
                .error(placeholder)
                .transform(CenterCrop(), RoundedCorners(16))

            Glide.with(activity)
                .load(albumArtUri)
                .apply(options)
                .into(findViewById(R.id.artist_image))
        }
    }
}
