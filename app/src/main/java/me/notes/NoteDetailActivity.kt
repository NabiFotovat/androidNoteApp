package me.notes

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.NavUtils
import android.view.MenuItem
import android.widget.EditText

import me.notes.sql.NotesDataSource

/**
 * An activity representing a single Note detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item contents are presented side-by-side with a list of items
 * in a [NoteListActivity].
 */
class NoteDetailActivity : AppCompatActivity() {
    private var noteId: Long = 0
    private var dataSource: NotesDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        val toolbar = findViewById(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        dataSource = NotesDataSource(this)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(MyOnClickListener())

        // Show the Up button in the action bar.
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()

            if (intent.hasExtra(NoteDetailFragment.ARG_ITEM_ID)) {
                noteId = intent.getLongExtra(NoteDetailFragment.ARG_ITEM_ID, 999)
                arguments.putLong(ARG_ITEM_ID,
                        noteId)
            }

            val fragment = NoteDetailFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.note_detail_container,
                            fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more contents, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, Intent(this, NoteListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MyOnClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val content = findViewById(R.id.note_detail) as EditText
            dataSource!!.updateContent(noteId, content.text.toString())
            startActivity(Intent(view.context, NoteListActivity::class.java))
        }
    }

    companion object {

        val ARG_ITEM_ID = "item_id"
    }
}
