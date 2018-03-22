
public class GeoSearch {

	private double latitude, longitude;

	public GeoSearch(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;

		String allTweetsText = searchTweetsByLocation();
	}

	private String searchTweetsByLocation() {
		String searchURL = createURLForLocation();
		return null;
	}

	private String createURLForLocation() {
		String url = "https://api.twitter.com/1.1/search/tweets.json?geocode=" + latitude + "," + longitude
				+ ",1mi&count=100";
		return null;
	}
}
