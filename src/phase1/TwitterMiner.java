package phase1;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brice on 31/03/2017.
 */
public class TwitterMiner {

    private final String consumerKey = "alaI4sKe5SEij1r2mImB6zmp7";
    private final String consumerSecret = "poiRSlASrR5SPH9nqJdUv19IbM0q9kEbnRIREwYMvvTsZjS3Ib";
    private final String accessToken = "263085413-42mpDDG74pNoFixQTBFT4Rpm7PBW4T2wU8kw1jf3";
    private final String accessTokenSecret = "weet4CKY6cyvk0Nl6tiqtKTJP6lsnETcmLIlTpWDpFolA";

    private Twitter twitter;

    public TwitterMiner() {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret);

        TwitterFactory twitterFactory = new TwitterFactory(config.build());
        twitter =  twitterFactory.getInstance();
    }

    public List<Status> search(String hashTag, int number) throws TwitterException {
        Query request = new Query(hashTag);
        request.count(number);

        QueryResult result = twitter.search(request);
        List<Status> Tweets = new ArrayList(result.getTweets());

        return Tweets;
    }
}
