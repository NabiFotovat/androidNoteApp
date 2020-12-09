package me.notes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class NoteClickListener implements View.OnClickListener {

   private Long noteId;

    public NoteClickListener(Long noteId) {
       this.noteId = noteId;
    }

    @Override
    public void onClick(View view) {
        Log.d("------", "Clicked on NoteId " + noteId);
        Context context = view.getContext();
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra(NoteDetailFragment.ARG_ITEM_ID, noteId);

        context.startActivity(intent);
    }
}
