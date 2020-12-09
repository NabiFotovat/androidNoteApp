package me.notes

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText

import me.notes.dataobjects.Note
import me.notes.sql.NotesDataSource

class NoteDetailAppBarListener(private val dataSource: NotesDataSource, private val context: Context, private val note: Note) : View.OnClickListener {

    override fun onClick(view: View) {
        val alert = AlertDialog.Builder(view.context)

        alert.setTitle("Edit Title")

        // Set an EditText view to get user input
        val input = EditText(view.context)
        input.setTextColor(Color.BLACK)
        alert.setView(input)

        alert.setPositiveButton("Ok") { dialog, whichButton ->
            val newNoteText = input.text.toString()
            dataSource.updateTitle(note.id, newNoteText)

            val intent = Intent(context, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailFragment.ARG_ITEM_ID, note.id)
            context.startActivity(intent)
        }

        alert.setNegativeButton("Cancel") { dialog, whichButton ->
            // Canceled.
        }

        alert.show()
    }
}
