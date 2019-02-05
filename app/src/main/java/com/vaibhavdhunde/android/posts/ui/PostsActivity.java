package com.vaibhavdhunde.android.posts.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vaibhavdhunde.android.posts.R;
import com.vaibhavdhunde.android.posts.adapter.PostAdapter;
import com.vaibhavdhunde.android.posts.model.Post;
import com.vaibhavdhunde.android.posts.network.JsonPlaceholderApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class PostsActivity extends AppCompatActivity {

    private static final String POSTS_BASE_URL = "https://jsonplaceholder.typicode.com/";

    private static final String KEY_CURRENT_POSTS = "KEY_CURRENT_POSTS";

    private List<Post> mPosts;

    @BindView(R.id.list_posts)
    RecyclerView mListPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupListPosts();

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURRENT_POSTS)) {
            mPosts = savedInstanceState.getParcelableArrayList(KEY_CURRENT_POSTS);
            loadPosts();
        } else {
            if (hasConnection()) {
                fetchPosts();
            } else {
                Toast.makeText(this, R.string.error_no_internet_connection,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupListPosts() {
        mListPosts.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(this);
        mListPosts.setLayoutManager(layoutManager);
        mListPosts.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
    }

    private void fetchPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(POSTS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        Call<List<Post>> call = jsonPlaceholderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Timber.d(getString(R.string.error_fetch_posts_reason), response.message());
                    return;
                }

                mPosts = response.body();
                loadPosts();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Timber.d(getString(R.string.error_fetch_posts_reason), t.getMessage());
                Toast.makeText(PostsActivity.this, R.string.error_fetch_posts,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosts() {
        PostAdapter postAdapter = new PostAdapter(mPosts);
        mListPosts.setAdapter(postAdapter);
    }

    private boolean hasConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(
                KEY_CURRENT_POSTS,
                (ArrayList<? extends Parcelable>) mPosts);
        super.onSaveInstanceState(currentState);
    }
}
