package cz.sk_net.eyeinthesky;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;

public class HttpClient extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            url.openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
