package me.notes

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView

import me.notes.dataobjects.Note

class NoteViewHolder
//    public Note item;

(view: View) : RecyclerView.ViewHolder(view) {
    //    public final View view;
    val titleView: TextView
    val contentsView: TextView
    val handle: ImageView

    init {
        //        this.view = view;
        titleView = view.findViewById(R.id.note_title) as TextView
        contentsView = view.findViewById(R.id.note_contents) as TextView
        handle = view.findViewById(R.id.handle) as ImageView
        //        view.setAnimation(new Animation() {
        //        });
    }

    override fun toString(): String {
        return super.toString() + " '" + contentsView.text + "'"
    }
}

