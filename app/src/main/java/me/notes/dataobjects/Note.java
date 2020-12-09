package me.notes.dataobjects;

public class Note {

    public final long id;
    public final String title;
    public final String contents;
    public int rank;

    public Note(long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    @Override
    public String toString() {
        return title;
    }
}
