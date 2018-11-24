package ch.techteam.techteamlauzhack;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class SpotifySingleton {

    private static SpotifySingleton instance_;
    private SpotifyAppRemote spotifyAppRemote_;
    private String accessToken_;

    private SpotifySingleton() {
        //spotifyAppRemote_ = null;
        accessToken_ = null;
    }

    /*public static void putAppRemote(SpotifyAppRemote spotifyAppRemote) {
        if (instance_ == null) {
            instance_ = new SpotifySingleton();
        }
        instance_.spotifyAppRemote_ = spotifyAppRemote;

    }*/


    /*public SpotifyAppRemote getSpotifyAppRemote_() {
        return spotifyAppRemote_;
    }*/

    public static SpotifySingleton get() {
        if (instance_ == null) {
            instance_ = new SpotifySingleton();
        }
        return instance_;
    }

    public static void putAccessToken(String accessToken) {
        if (instance_ == null) {
            instance_ = new SpotifySingleton();
        }
        instance_.accessToken_ = accessToken;

    }

        public String getAccessToken() {
        return accessToken_;
    }
}