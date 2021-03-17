import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

public class youtubeUtils {
public static void search(String term) throws IOException {
	String data ="";
	Document doc = Jsoup.connect("https://www.youtube.com/results?search_query="+term).get();
	//System.out.println(doc);
	Elements videos = doc.select("script");
	for (Element video : videos) {
		if ((video.toString().contains("var ytInitialData ="))) {
			data = video.data().substring(20);
		}
	}
	JSONArray ja = new JSONObject(data).getJSONObject("contents").getJSONObject("twoColumnSearchResultsRenderer").getJSONObject("primaryContents").getJSONObject("sectionListRenderer").getJSONArray("contents").getJSONObject(0).getJSONObject("itemSectionRenderer").getJSONArray("contents");
	for (int i = 0; i < ja.length(); i++) {
		JSONObject jo = ja.getJSONObject(i);
		if(jo.has("videoRenderer")) {
		jo =jo.getJSONObject("videoRenderer");
		String id = jo.getString("videoId");
		String title = jo.getJSONObject("title").getJSONArray("runs").getJSONObject(0).getString("text");
		System.out.println(title+"\n"+id);
		}
	}
	

}


public static void main(String[] args) {
	try {
		search("Happy");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

}
