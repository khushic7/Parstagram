package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.ImageAdapter;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends PostsFragment {

    public static final String TAG = "ProfileFragment";
    private RecyclerView rvMyPosts;
    protected ImageAdapter adapter;
    protected List<Post> mPosts;
    private ImageView ivProfilePicture;
    private Button logoutButton;

    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ivProfilePicture = view.findViewById(R.id.iv_profilePictureForProfile);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.circleCrop();
        Glide.with(getContext()).load(ParseUser.getCurrentUser().getParseFile("profilePicture")
                .getUrl()).apply(options).into(ivProfilePicture);

        logoutButton = view.findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);

            }
        });

        rvMyPosts = view.findViewById(R.id.rv_myPosts);
        // Create the data source
        mPosts = new ArrayList<>();
        // Create the adapter
        adapter = new ImageAdapter(getContext(), mPosts, getFragmentManager());
        // Set the adapter on the recycler view
        rvMyPosts.setAdapter(adapter);
        // Set the layout manager on the recycler view
        rvMyPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryPosts();
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }
                mPosts.addAll(posts);
                adapter.notifyDataSetChanged();

                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    Log.d(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
            }
        });
    }
}
