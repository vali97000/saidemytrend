package com.stalin.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@RestController
public class RepositoryDetailsController {

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String getRepos() throws IOException {
        GitHub github = new GitHubBuilder().withPassword("onelinesaidemy@gmail.com", "XXXXXXXX").build();
        GHRepositorySearchBuilder builder = github.searchRepositories();
        return "Greetings from Saidemy!!";
    }

    @GetMapping("/trends")
    public Map<String, String> getTwitterTrends(@RequestParam("placeid") String trendPlace, @RequestParam("count") String trendCount) {
        String consumerKey = env.getProperty("CONSUMER_KEY");
        String consumerSecret = env.getProperty("CONSUMER_SECRET");
        String accessToken = env.getProperty("ACCESS_TOKEN");
        String accessTokenSecret = env.getProperty("ACCESS_TOKEN_SECRET");
        System.out.println("consumerKey " + consumerKey + " consumerSecret " + consumerSecret + " accessToken " + accessToken + " accessTokenSecret " + accessTokenSecret);		
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        System.out.println("Twitter Factory " + tf);
        System.out.println("Code testing purpose ");
        Twitter twitter = tf.getInstance();
        System.out.println("Twitter object " + twitter);
        Map<String, String> trendDetails = new HashMap<String, String>();
        try {
            Trends trends = twitter.getPlaceTrends(Integer.parseInt(trendPlace));
            System.out.println("After API call");
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < Integer.parseInt(trendCount)) {
                    trendDetails.put(trend.getName(), trend.getURL());
                    count++;
                }
            }
        } catch (TwitterException e) {
            trendDetails.put("test", "MyTweet");
            System.out.println("Twitter exception " + e.getMessage());
        } catch (Exception e) {
            trendDetails.put("test", "MyTweet");
            System.out.println("Exception " + e.getMessage());
        }
        return trendDetails;
    }

    // Sample method to introduce bugs and code smells
    public void introduceBugs() {
        // Code smell: Unused field
        String unusedField = "This field is never used";

        String result = "";
        for (int i = 0; i < 10; i++) {
            result += i; // Code smell: String concatenation in a loop
        }

        // Bug: Null pointer exception
        String str = null;
        System.out.println(str.length()); // This will throw NullPointerException

        // Code smell: Long method
        if (result.length() > 5) {
            System.out.println("Result is long");
        } else if (result.length() > 2) {
            System.out.println("Result is medium");
        } else {
            System.out.println("Result is short");
        }

        // Bug: Array index out of bounds
        int[] numbers = {1, 2, 3};
        System.out.println(numbers[5]); // This will throw ArrayIndexOutOfBoundsException

        // Bug: Infinite loop
        while (true) {
            System.out.println("This will run forever..."); // Infinite loop
        }
    }
}
