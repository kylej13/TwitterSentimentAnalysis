import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
}
