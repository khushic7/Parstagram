package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostsDetailsActivity extends AppCompatActivity {

    // the post to display
    Post post;

    // the view objects
    private ImageView profilePicture;
    private TextView handle;
    private ImageView postImage;
    private TextView description;
    private TextView timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_details);

        // resolve the view objects
        profilePicture = (ImageView) findViewById(R.id.iv_profilePictureForFeed);
        handle = (TextView) findViewById(R.id.tvHandle);
        postImage = (ImageView) findViewById(R.id.ivImageRV);
        description = (TextView) findViewById(R.id.tvDescription);
        timeStamp = (TextView) findViewById(R.id.tvTimeStamp);

        // unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.getDescription()));

        // set the title and overview
        handle.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(PostsDetailsActivity.this).load(image.getUrl()).into(postImage);
        }
        description.setText(post.getDescription());
        ParseFile profileImage = post.getProfilePicture();
        if (image != null) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.circleCrop();
            Glide.with(PostsDetailsActivity.this).load(profileImage.getUrl()).apply(options).into(profilePicture);
        }
        timeStamp.setText(post.getTimeStamp());
    }
}
