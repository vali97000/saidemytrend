package com.saidemy.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(RepositoryDetailsController.class);

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String getRepos() throws IOException {
        return "Greetings from Saidemy!!";
    }

    @GetMapping("/trends")
    public Map<String, String> getTwitterTrends(@RequestParam("placeid") String trendPlace, @RequestParam("count") String trendCount) {
        String consumerKey = env.getProperty("CONSUMER_KEY");
        String consumerSecret = env.getProperty("CONSUMER_SECRET");
        String accessToken = env.getProperty("ACCESS_TOKEN");
        String accessTokenSecret = env.getProperty("ACCESS_TOKEN_SECRET");
        log.info("consumerKey " + consumerKey + " consumerSecret " + consumerSecret + " accessToken " + accessToken + " accessTokenSecret " + accessTokenSecret);		
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        log.info("Twitter Factory " + tf);
        log.info("Code testing purpose ");
        Twitter twitter = tf.getInstance();
        log.info("Twitter object " + twitter);
        Map<String, String> trendDetails = new HashMap<String, String>();
        try {
            Trends trends = twitter.getPlaceTrends(Integer.parseInt(trendPlace));
            log.debug("After API call");
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < Integer.parseInt(trendCount)) {
                    trendDetails.put(trend.getName(), trend.getURL());
                    count++;
                }
            }
        } catch (TwitterException e) {
            trendDetails.put("test", "MyTweet");
            log.error("Twitter exception " + e.getMessage());
        } catch (Exception e) {
            trendDetails.put("test", "MyTweet");
            log.error("Exception " + e.getMessage());
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
        log.info(str.length()); // This will throw NullPointerException

        // Code smell: Long method
        if (result.length() > 5) {
            log.info("Result is long");
        } else if (result.length() > 2) {
            log.info("Result is medium");
        } else {
            log.info("Result is short");
        }

        // Bug: Array index out of bounds
        int[] numbers = {1, 2, 3};
        log.info(numbers[5]); // This will throw ArrayIndexOutOfBoundsException

        // Bug: Infinite loop
        while (true) {
            log.info("This will run forever..."); // Infinite loop
        }
    }
}
