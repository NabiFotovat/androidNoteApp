package me.notes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.note_list.*

import me.notes.sql.NotesDataSource

/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [NoteDetailActivity] representing
 * item contents. On tablets, the activity presents the list of items and
 * item contents side-by-side using two vertical panes.
 */
class NoteListActivity : AppCompatActivity(), OnStartDragListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private var dataSource: NotesDataSource? = null
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        dataSource = NotesDataSource(this)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            val ctx = view.context
            val intent = Intent(ctx, NoteDetailActivity::class.java)
            val newNoteId = dataSource!!.addNote("Title", "")
            intent.putExtra(NoteDetailFragment.ARG_ITEM_ID, newNoteId)
            ctx.startActivity(intent)
        }

        val adapter = RecyclerViewAdapter(this, dataSource!!, this)
        (note_list as RecyclerView).adapter = adapter

        //        adapter.notifyItemChanged(99);

        val callback = NoteTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(note_list)

        if (note_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
