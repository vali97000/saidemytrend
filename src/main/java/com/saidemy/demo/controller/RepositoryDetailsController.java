package com.saidemy.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // Example only – if you don't use GitHub here, remove these lines
        GitHub github = new GitHubBuilder().withOAuthToken(env.getProperty("GITHUB_TOKEN")).build();
        log.info("GitHub connection established: {}", github.getApiUrl());
        return "Greetings from Saidemy!!";
    }

    @GetMapping("/trends")
    public Map<String, String> getTwitterTrends(
            @RequestParam("placeid") String trendPlace,
            @RequestParam("count") String trendCount) {

        String consumerKey = env.getProperty("CONSUMER_KEY");
        String consumerSecret = env.getProperty("CONSUMER_SECRET");
        String accessToken = env.getProperty("ACCESS_TOKEN");
        String accessTokenSecret = env.getProperty("ACCESS_TOKEN_SECRET");

        log.info("Twitter credentials loaded successfully");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Map<String, String> trendDetails = new HashMap<>();
        try {
            Trends trends = twitter.getPlaceTrends(Integer.parseInt(trendPlace));
            log.debug("Fetched trends for placeId={}", trendPlace);
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < Integer.parseInt(trendCount)) {
                    trendDetails.put(trend.getName(), trend.getURL());
                    count++;
                }
            }
        } catch (TwitterException e) {
            trendDetails.put("error", "Twitter API failed");
            log.error("Twitter exception {}", e.getMessage());
        } catch (Exception e) {
            trendDetails.put("error", "Unexpected error");
            log.error("Exception {}", e.getMessage());
        }
        return trendDetails;
    }

    // This method is intentionally buggy for Sonar demonstration, but must compile
    public void introduceBugs() {
        String unusedField = "This field is never used";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            result.append(i);
        }

        // Commenting out actual runtime exceptions to avoid Jenkins failure
        // String str = null;
        // log.info("Length: {}", str.length());
        // int[] numbers = {1, 2, 3};
        // log.info("Number: {}", numbers[5]);

        if (result.length() > 5) {
            log.info("Result is long");
        } else if (result.length() > 2) {
            log.info("Result is medium");
        } else {
            log.info("Result is short");
        }

        // Avoid infinite loop for pipeline safety
        log.info("Skipping infinite loop for Jenkins run");
    }
}
