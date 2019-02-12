import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

public class GeoSearch {

	private double latitude, longitude;
	private List<ToneScore> toneScores;

	public GeoSearch(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;

		String allTweetsText = searchTweetsByLocation();
		ToneAnalysis result = analyzeText(allTweetsText);

		DocumentAnalysis da = result.getDocumentTone();
		List<ToneScore> toneScores = da.getTones();
		this.toneScores = toneScores;
	}

	private String searchTweetsByLocation() {
		String searchURL = createURLForLocation();

		JSONObject response = Utils.buildAndSendRequest(searchURL);

		StringBuilder allTweets = new StringBuilder();
		try {
			JSONArray statuses = response.getJSONArray("statuses");
			System.out.println("Number of tweets: " + statuses.length());
			for (int x = 0; x < statuses.length(); x++) {
				JSONObject tweet = (JSONObject) statuses.get(x);
				String tweetText = tweet.getString("full_text");
				allTweets.append(tweetText + " ");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allTweets.toString();
	}

	private String createURLForLocation() {
		String url = "https://api.twitter.com/1.1/search/tweets.json?geocode=" + latitude + "," + longitude
				+ ",1mi&count=100&tweet_mode=extended";
		return url;
	}

	private ToneAnalysis analyzeText(String text) {
		ToneAnalyzer service = new ToneAnalyzer("2017-09-21", "d74d1640-78d7-4c0b-ba88-ed16e9935e2d", "05icUbUbAXrJ");
		ToneOptions toneOptions = new ToneOptions.Builder().html(text).addTone(ToneOptions.Tone.EMOTION).build();

		ToneAnalysis result = service.tone(toneOptions).execute();
		return result;
	}

	public void printToneScores() {
		System.out.println("Sentiment score for Latitude: " + latitude + " Longitude: " + longitude);
		for (ToneScore score : toneScores) {
			System.out.println(score.getToneName() + ": " + score.getScore());
		}
	}

	public List<ToneScore> getToneScores() {
		return toneScores;
	}
}
