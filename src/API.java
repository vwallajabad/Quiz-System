import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class API {
    private URL url;
    private String url_;
    private URLConnection urlConnection;
    private BufferedReader bufferedReader;
    public StringBuilder stringBuilder;

    public API(String _url) {
        url_ = _url;
        fetchData();
    }

    private void fetchData() {
        try {
            url = new URL(url_);
            urlConnection = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        fetchData();
    }
}
