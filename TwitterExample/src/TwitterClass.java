
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
		String url = createRequestURLForUser("BarackObama");

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

		try {
			JSONObject obj = new JSONObject(response);
			JSONArray statuses = obj.getJSONArray("statuses");
			System.out.println("Number of tweets: " + statuses.length());
			for (int x = 0; x < statuses.length(); x++) {
				JSONObject tweet = (JSONObject) statuses.get(x);
				String tweetText = tweet.getString("full_text");
				sb.append(tweetText + " ");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String allTweetsText = sb.toString();
		// JSONObject watsonRequest = createWatsonJSON(allTweetsText);

		ToneAnalyzer service = new ToneAnalyzer("2017-09-21", "d74d1640-78d7-4c0b-ba88-ed16e9935e2d", "05icUbUbAXrJ");
		ToneOptions toneOptions = new ToneOptions.Builder().html(allTweetsText).addTone(ToneOptions.Tone.EMOTION)
				.addTone(ToneOptions.Tone.LANGUAGE).addTone(ToneOptions.Tone.SOCIAL).build();
		System.out.println("Tones:");
		for (String tone : toneOptions.tones()) {
			System.out.println(tone);
		}
		ToneAnalysis result = service.tone(toneOptions).execute();
		System.out.println("\n\n Sentences score");
		for (SentenceAnalysis sa : result.getSentencesTone()) {
			for (ToneScore ts : sa.getTones()) {
				System.out.println(ts.getToneName() + ": " + ts.getScore());
			}
		}

		// System.out.println(result);

	}

	/**
	 * public static void main(String[] args) { ConfigurationBuilder cb = new
	 * ConfigurationBuilder();
	 * cb.setDebugEnabled(true).setOAuthConsumerKey("gMQMgbfwB7tIQ4ywoEDyp7CR9")
	 * .setOAuthConsumerSecret("TI10f56Awm7r8NVkSAdBHMoIWQFRJbXzQKQun6nYMLZkk2QWow")
	 * .setOAuthAccessToken("926144203071115265-ZBiOBu4tRo617dGu2kJuOTfuBMoApEg")
	 * .setOAuthAccessTokenSecret("Y42ysR3XXd8k5YAwnUcdQAXwQLYR2FSqrvfZ14t2s7IXn");
	 * TwitterFactory tf = new TwitterFactory(cb.build()); Twitter twitter =
	 * tf.getInstance();
	 * 
	 * Query query = new Query("source:twitter4j baeldung"); QueryResult result; try
	 * { //Paging paging = new Paging(1, 1); List<Status> statuses =
	 * twitter.getUserTimeline("realDonaldTrump"); //twitter.getUserT
	 * 
	 * // result = twitter.search(query); System.out.println("Search results: " +
	 * statuses.size()); for (Status status : statuses) { status. //status.get
	 * System.out.println(status.getText()); JSONObject obj =
	 * createWatsonJSON(status.getText()); } } catch (TwitterException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 **/

	private static String createRequestURLForUser(String username) {
		String urlString = "https://api.twitter.com/1.1/search/tweets.json?q=" + username
				+ "&result_type=popular&tweet_mode=extended";
		return urlString;
	}

	private static JSONObject createWatsonJSON(String textToAnalyze) {
		String jsonString = "{\"textToAnalyze\": \"" + textToAnalyze + "\","
				+ " \"username\"     : \"d74d1640-78d7-4c0b-ba88-ed16e9935e2d\","
				+ " \"password\"     : \"05icUbUbAXrJ\","
				+ " \"endpoint\"     : \"https://sandbox-watson-proxy.mybluemix.net/tone-analyzer/api\","
				+ " \"skip_authentication\": \"true\"}";
		try {
			JSONObject obj = new JSONObject(jsonString);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
}
