import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;


import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class YTCommentCrawler {
   
    private static final String DEVELOPER_KEY = "AIzaSyAomqNsYSaPsN7hSkAB2lRPwtSyyMJlyVY";
    private static final String APPLICATION_NAME = "YTCrawlCrawl";
   // private static final JsonFactory JSON_FACTORY = new JacksonFactory().getDefaultInstance();
   
    YTCommentCrawler() throws GoogleJsonResponseException, GeneralSecurityException, IOException{
    	myYouTubeCrawl();
    }
    
    private static void myYouTubeCrawl() throws GeneralSecurityException, IOException, GoogleJsonResponseException{
    	 YouTube youtubeService = getService();
         // Define and execute the API request
         YouTube.CommentThreads.List request = youtubeService.commentThreads().list("id,snippet");
         CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
             .setMaxResults(20L)//<--- limit set to 100
             .setVideoId("eq5kdb6uIZQ") //this is the video code <----
             .execute();
         
         List<CommentThread> channelComments = response.getItems();
         CommentSnippet snippet;
         
         if (channelComments.isEmpty()) {
             System.out.println("Can't get channel comments.");
         } else {

         	  
            SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
     		sentimentAnalyzer.initialize();
     		
     		
             System.out
                     .println("\n================== Generating Sentimental Assessment ==================\n");
             for (CommentThread channelComment : channelComments) {
                 snippet = channelComment.getSnippet().getTopLevelComment()
                         .getSnippet();
               
                 SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(snippet.getTextDisplay());
 	    		System.out.println(snippet.getAuthorDisplayName()
 	    				+ "\n"
 	    				+ snippet.getTextDisplay() + "\n" 
 	    				+ "\n" 
 	    				+ "Sentiment Type: " + sentimentResult.getSentimentType() + "\n");

             }
         }
         
    }

    public static YouTube getService() throws GeneralSecurityException, IOException {
  
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
        Credential credential = Auth.authorize(scopes, "commentthreads");
        
        return new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
        
    }

   
}