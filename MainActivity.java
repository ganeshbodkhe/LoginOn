import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.Arrays;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class MainActivity extends FragmentActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mLU8AWe0R3NVuGtWctRDHfSIY";
    private static final String TWITTER_SECRET = "w8tMdYy94Z9PKAf4S1QkjfooXzk13PqgylAdrC0MNzpnYqZyw9";

    private TwitterLoginButton loginButton;
    private LoginButton loginBtn;
    private TextView username,twitterID;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        username = (TextView) findViewById(R.id.username);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    username.setText("Login as " + user.getName());

                    Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/groups/fbdevelopers/permalink/882517998458382/"));
                    startActivity(i);


                } else {
                    username.setText("Logout");
                }
            }
        });



        //Twitter
        twitterID = (TextView)findViewById(R.id.twitterid);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                setUpViewsForTweetComposer();


                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                twitterID.setText("Twitter ID " + msg);
               // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException e) {
                Log.d("TwitterKit", "Login with Twitter failure", e);
            }

//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("TwitterKit", "Login with Twitter failure", exception);
//            }
        });

    }

    private void setUpViewsForTweetComposer() {

        TweetComposer.Builder builder = new TweetComposer.Builder(this).text("Hi !");

        builder.show();
    }


    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}
