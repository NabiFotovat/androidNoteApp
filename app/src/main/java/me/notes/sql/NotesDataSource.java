package me.notes.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.notes.dataobjects.Note;

public class NotesDataSource {

    private SQLiteDatabase database;
    private SqlHelper dbHelper;

    public NotesDataSource(Context context) {
        dbHelper = new SqlHelper(context);
        database = dbHelper.getWritableDatabase(); //TODO Async
//        dbHelper.onCreate(database); //TODO remove
    }

    private int getMaxRank() {
        Cursor cursor = database.rawQuery("select max(rank) as max_rank from notes;", null);
        if (cursor != null && cursor.moveToFirst()) {
            int res = cursor.getInt(0);
            cursor.close();
            return res;
        }
        return -1;
    }

    public void updateRankToNewHighest(long id) {
        Log.d("----", "Updating Rank of Item " + id + " to " + new Integer(getMaxRank()));
        updateRankToNewHighest(id, getMaxRank() + 1);
    }

    public void updateRankToNewHighest(long id, int rank) {
        try {
            database.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("rank", rank);
            database.update("notes",
                    values,
                    "id = ?",
                    new String[]{new Long(id).toString()}
            );

            database.setTransactionSuccessful();
        } catch (Exception e) {
            database.endTransaction();
            throw e;
        }
        database.endTransaction();
    }

    public void updateTitle(long id, String title) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("rank", getMaxRank() + 1);
        database.update("notes",
                values,
                "id = ?",
                new String[]{new Long(id).toString()}
        );
    }

    public void updateContent(long id, String content) {
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("rank", getMaxRank() + 1);
        database.update("notes",
                values,
                "id = ? ",
                new String[]{new Long(id).toString()}
        );
    }

    public void deleteNote(long id) {
        database.delete(
                "notes",
                "id =?",
                new String[]{Long.valueOf(id).toString()}
        );
    }

    public List<Note> getNotes() {

        Log.d("----", "Fetching notes from Db");

        Cursor cursor = database.rawQuery("select * from notes order by rank desc;", null);

        List<Note> allNotes = new ArrayList<>();

        int count = cursor.getCount();
        int counter = 0;

        cursor.moveToFirst();
        while (counter < count) {
            allNotes.add(counter, getNote(cursor));
            cursor.moveToNext();
            counter++;
        }
        return allNotes;
    }

    public long addNote(String title, String content) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("rank", getMaxRank() + 1);

        return database.insert(
                "notes",
                null,
                values);
    }

    public Note getNote(String title) {

        String[] projection = {
                "id",
                "title",
                "content"
        };

        Cursor cursor = database.query(
                "notes",                // The table to query
                projection,             // The columns to return
                "title = ?",            // The WHERE clause
                new String[]{title},    // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );
        cursor.moveToFirst();
        return getNote(cursor);
    }

    public Note getNote(long id) {

        String[] projection = {
                "id",
                "title",
                "content"
        };

        Cursor cursor = database.query(
                "notes",                                // The table to query
                projection,                             // The columns to return
                "id = ?",                               // The WHERE clause
                new String[]{new Long(id).toString()},  // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        cursor.moveToFirst();
        return getNote(cursor);
    }

    public void dropTable() {
        database.execSQL("drop table notes;");
    }

    public void cleanTable() {
        database.delete("notes", null, null);
    }

    private Note getNote(Cursor cursor) {

        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow("id")
        );
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow("title")
        );
        String content = cursor.getString(
                cursor.getColumnIndexOrThrow("content")
        );

        return new Note(itemId, title, content);
    }
}
