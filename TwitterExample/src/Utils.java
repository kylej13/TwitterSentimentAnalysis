import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class Utils {

	public static final String AccessToken = "926144203071115265-ZBiOBu4tRo617dGu2kJuOTfuBMoApEg";
	public static final String AccessSecret = "Y42ysR3XXd8k5YAwnUcdQAXwQLYR2FSqrvfZ14t2s7IXn";
	public static final String ConsumerKey = "gMQMgbfwB7tIQ4ywoEDyp7CR9";
	public static final String ConsumerSecret = "TI10f56Awm7r8NVkSAdBHMoIWQFRJbXzQKQun6nYMLZkk2QWow";

	public static final HashMap<String, LatLng> cityLocations;
	public static final List<String> cityNames;

	static {
		cityNames = new ArrayList<String>();
		cityNames.add("Raleigh");
		cityLocations = new HashMap<String, LatLng>();
		cityLocations.put("Raleigh", new LatLng(35.787743, -78.644257));
	}

	public static JSONObject buildAndSendRequest(String url) {
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Utils.ConsumerKey, Utils.ConsumerSecret);
		consumer.setTokenWithSecret(Utils.AccessToken, Utils.AccessSecret);

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
			JSONObject responseJSON = new JSONObject(response);
			return responseJSON;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
}
