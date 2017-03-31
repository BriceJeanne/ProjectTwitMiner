/*
    Created by Brice JEANNE
    31/03/2017
*/

package main;


import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class main {
    // Déclaration
    //Objs
    TwitterFactory twitterFactory;
    Twitter twitter;
    RequestToken requestToken;

    // Requete
    Query request;
    QueryResult QResult;

    ArrayList Tweets;
    String msg, user, pseudo;
    Date date;
    // Cstd


    void Connect() {
        // Auth
        ConfigurationBuilder Config = new ConfigurationBuilder();
        Config.setOAuthConsumerKey("alaI4sKe5SEij1r2mImB6zmp7")
                .setOAuthConsumerSecret("poiRSlASrR5SPH9nqJdUv19IbM0q9kEbnRIREwYMvvTsZjS3Ib")
                .setOAuthAccessToken("263085413-42mpDDG74pNoFixQTBFT4Rpm7PBW4T2wU8kw1jf3")
                .setOAuthAccessTokenSecret("weet4CKY6cyvk0Nl6tiqtKTJP6lsnETcmLIlTpWDpFolA");
        twitterFactory = new TwitterFactory(Config.build());
        twitter =  twitterFactory.getInstance();
    };


    void Research(String Hashtag) {
        try {

            request = new Query(Hashtag);
            QResult = twitter.search(request);

            Tweets = (ArrayList) QResult.getTweets(); // Séparation de chaque tweet

            // Traitement des tweets
            PrintWriter w =  new PrintWriter("tweets.csv", "UTF-8");
            for (int i = 0; i < Tweets.size(); i++) {
                Status t = (Status)Tweets.get(i);

                user = t.getUser().getName();
                msg = t.getText();
                pseudo =  t.getUser().getScreenName();
                date = t.getCreatedAt();
                System.out.print("USER :" + user + "msg: "+ msg + "pseudo :" + pseudo + "date :"+date);
            }
        } catch (TwitterException e) {
            System.out.println("Erreur de connection : " + e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
            String hashtag = '#' + JOptionPane.showInputDialog(" Insérez le Hashtag à rechercher : #");
            main m = new main();
            m.Connect();
            m.Research(hashtag);
        }

    }
