/*
    Created by Brice JEANNE
    31/03/2017
*/

package main;


import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.*;
import java.io.FileWriter;
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

    FileWriter filewriter = null;
    // Requete
    Query request;
    QueryResult QResult;

    ArrayList Tweets;
    String msg, user, pseudo;
    Date date;
    // Cstd

    static String COMMA_DELIMITER="," ;
    static String NEW_LINE_SEPARATOR="\n";
    static String  filename = "fichier.csv";
    static String FILE_HEADER = "Date , @username , message";

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
            filewriter = new FileWriter(filename);
            filewriter.append(FILE_HEADER);
            for (int i = 0; i < Tweets.size(); i++) {
                Status t = (Status)Tweets.get(i);

                user = t.getUser().getName();
                msg = t.getText();
                msg = msg.replace(',',';');
                msg = msg.replace(' ', ';');
                msg = msg.replace('"',';');
                msg = msg.replace('\n', ';');
                pseudo =  '@' + t.getUser().getScreenName();
                date = t.getCreatedAt();
                System.out.println("USER :" + user);
                System.out.println("Msg: "+ msg );
                System.out.println("Pseudo :" + pseudo);
                System.out.println("Date :"+date);
                filewriter.append(NEW_LINE_SEPARATOR);
                filewriter.append(String.valueOf(date));
                filewriter.append(COMMA_DELIMITER);
                filewriter.append(pseudo);
                filewriter.append(COMMA_DELIMITER);
                filewriter.append(msg);
                filewriter.append(COMMA_DELIMITER);
            }
        } catch (TwitterException e) {
            System.out.println("Erreur de connection : " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                filewriter.flush();
                filewriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
            String hashtag = '#' + JOptionPane.showInputDialog(" Insérez le Hashtag à rechercher : #");
            main m = new main();
            m.Connect();
            m.Research(hashtag);
        }

    }
