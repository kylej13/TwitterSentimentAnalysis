import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

	public static final HashMap<String, LatLng> cityLocations;
	public static final List<String> cityNames;

	static {
		cityNames = new ArrayList<String>();
		cityNames.add("Raleigh");
		cityLocations = new HashMap<String, LatLng>();
		cityLocations.put("Raleigh", new LatLng(35.787743, -78.644257));
	}
}
