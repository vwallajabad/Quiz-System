import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class BonusAPI extends API {

    private JSONObject jsonObject;
    private JSONArray tossupsArray;

    private ArrayList<String> bonusQuestions = new ArrayList<String>();
    private ArrayList<String> bonusAnswers = new ArrayList<String>();

    private JSONObject jsondata;

    public BonusAPI(String url_) {
        super(url_);
        jsonObject = new JSONObject(super.stringBuilder.toString());
        tossupsArray = jsonObject.getJSONArray("bonuses");
        jsondata = tossupsArray.getJSONObject(0);
    }

    public String getLeadIn() {
        return jsondata.getString("leadin");
    }

    public ArrayList<String> getBonusQuestionsArrayList() {
        bonusQuestions.clear();
        JSONArray partsArray = jsondata.getJSONArray("parts");
        for (int i = 0; i < partsArray.length(); i++) {
            try {
                String part = partsArray.getString(i);
                bonusQuestions.add(part);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bonusQuestions;
    }

    public ArrayList<String> getBonusAnswersArrayList() {
        bonusAnswers.clear();
        JSONArray answersArray = jsondata.getJSONArray("answers");
        for (int i = 0; i < answersArray.length(); i++) {
            try {
                String part = answersArray.getString(i);
                bonusAnswers.add(part);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return bonusAnswers;
    }

    @Override
    public void refresh() {
        super.refresh();
        jsonObject = new JSONObject(super.stringBuilder.toString());
        tossupsArray = jsonObject.getJSONArray("bonuses");
        jsondata = tossupsArray.getJSONObject(0);
    }

}
