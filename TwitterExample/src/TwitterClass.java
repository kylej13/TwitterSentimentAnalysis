
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class TwitterClass {

	private static String AccessToken = "926144203071115265-ZBiOBu4tRo617dGu2kJuOTfuBMoApEg";
	private static String AccessSecret = "Y42ysR3XXd8k5YAwnUcdQAXwQLYR2FSqrvfZ14t2s7IXn";
	private static String ConsumerKey = "gMQMgbfwB7tIQ4ywoEDyp7CR9";
	private static String ConsumerSecret = "TI10f56Awm7r8NVkSAdBHMoIWQFRJbXzQKQun6nYMLZkk2QWow";

	public static void main(String[] args) {

		String allTweetsText = retrieveTweetsFromUsername("BarackObama");
		ToneAnalysis result = analyzeText(allTweetsText);

		DocumentAnalysis da = result.getDocumentTone();
		System.out.println("\n\n Document score");
		List<ToneScore> toneScores = da.getTones();

		for (ToneScore score : toneScores) {
			System.out.println(score.getToneName() + ": " + score.getScore());
		}
	}

	private static String retrieveTweetsFromUsername(String username) {
		String url = createRequestURLForUsername(username);
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret);
		consumer.setTokenWithSecret(AccessToken, AccessSecret);

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		try {
			consumer.sign(request);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e1) {
			e1.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		try {
			HttpResponse response = client.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String response = sb.toString();

		StringBuilder allTweets = new StringBuilder();

		try {
			JSONObject obj = new JSONObject(response);
			JSONArray statuses = obj.getJSONArray("statuses");
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

	private static ToneAnalysis analyzeText(String text) {
		ToneAnalyzer service = new ToneAnalyzer("2017-09-21", "d74d1640-78d7-4c0b-ba88-ed16e9935e2d", "05icUbUbAXrJ");
		ToneOptions toneOptions = new ToneOptions.Builder().html(text).addTone(ToneOptions.Tone.EMOTION).build();
		System.out.println("Tones:");
		for (String tone : toneOptions.tones()) {
			System.out.println(tone);
		}
		ToneAnalysis result = service.tone(toneOptions).execute();
		System.out.println("\n\n Sentences score");

		return result;
	}

	private static String createRequestURLForUsername(String username) {
		String urlString = "https://api.twitter.com/1.1/search/tweets.json?q=" + username
				+ "&result_type=popular&tweet_mode=extended";
		return urlString;
	}

}
