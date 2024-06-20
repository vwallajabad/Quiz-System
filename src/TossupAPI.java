import org.json.JSONArray;
import org.json.JSONObject;

public class TossupAPI extends API {
    private JSONObject jsonObject;
    private JSONArray tossupsArray;
    private JSONObject firstTossup;

    public TossupAPI(String url) {
        super(url);
        parseData();
    }

    private void parseData() {
        jsonObject = new JSONObject(super.stringBuilder.toString());
        tossupsArray = jsonObject.getJSONArray("tossups");
        firstTossup = tossupsArray.getJSONObject(0);
    }

    @Override
    public void refresh() {
        super.refresh();
        parseData();
    }

    public String getQuestion() {
        return firstTossup.getString("question");
    }

    public String getAnswer() {
        return firstTossup.getString("answer");
    }
}
