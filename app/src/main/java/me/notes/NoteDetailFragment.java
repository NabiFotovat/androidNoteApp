package me.notes;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import me.notes.dataobjects.Note;
import me.notes.sql.NotesDataSource;

public class NoteDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Note note;
    private NotesDataSource dataSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new NotesDataSource(getContext());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy title specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load title from a title provider.
            note = dataSource.getNote(
                    getArguments()
                            .getLong(ARG_ITEM_ID)
                    );

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            appBarLayout.setOnClickListener(new NoteDetailAppBarListener(dataSource, getContext(), note));

            if (appBarLayout != null) {
                appBarLayout.setTitle(note.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_detail, container, false);

        if (note != null) {
            EditText view = ((EditText) rootView.findViewById(R.id.note_detail));
            view.setText(note.contents);

            view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    boolean handled = false;
                    Log.d("actionId", Integer.valueOf(actionId).toString());
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        dataSource.updateContent(note.id, textView.getText().toString());
                        handled = true;
                    }
                    return handled;
                }
            });
        }

        return rootView;
    }
}
