package project.movinindoor;

import android.os.AsyncTask;

/**
 * Created by Davey on 3-12-2014.
 */
public class LoadRooms extends AsyncTask<String, Void, Rooms> {
    @Override
    protected Rooms doInBackground(String... params) {
        Rooms rooms = new Rooms();
        return rooms;
    }
}
