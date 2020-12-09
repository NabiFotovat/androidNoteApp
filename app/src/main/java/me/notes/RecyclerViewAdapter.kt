package me.notes

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.view.MotionEventCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import java.util.Collections

import me.notes.dataobjects.Note
import me.notes.sql.NotesDataSource

class RecyclerViewAdapter(
    private val context: Context,
    private val dataSource: NotesDataSource,
    private val dragStartListener: OnStartDragListener
) : RecyclerView.Adapter<NoteViewHolder>(), ItemTouchHelperAdapter {

    private val values: List<Note> = dataSource.notes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_list_content, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = values[position]
        //        holder.item = note;
        holder.titleView.text = note.title
        holder.contentsView.text = note.contents

        holder.handle.setOnTouchListener { _, event ->
            dragStartListener.onStartDrag(holder)
            false
        }

        holder.titleView.setOnClickListener(NoteClickListener(note.id))
        holder.contentsView.setOnClickListener(NoteClickListener(note.id))
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Log.d("DEBUG", "Moving from position $fromPosition to position $toPosition")
        dataSource.updateRankToNewHighest(values[fromPosition].id)
        Collections.swap(values, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val note = values[position]
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete the item?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                deleteItem(note.id)
                notifyItemRemoved(position)
            }
            .setNegativeButton("No") { dialog, id ->
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Private Methods

    private fun deleteItem(noteId: Long?) {
        Log.d(noteId!!.toString(), "deleted")
        dataSource.deleteNote(noteId)
        context.startActivity(Intent(context, NoteListActivity::class.java))
    }
}
