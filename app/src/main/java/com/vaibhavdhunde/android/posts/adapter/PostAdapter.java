package com.vaibhavdhunde.android.posts.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vaibhavdhunde.android.posts.R;
import com.vaibhavdhunde.android.posts.model.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> mPosts;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int position) {
        Post currentPost = mPosts.get(position);
        postViewHolder.mDisplayUserId.setText(String.valueOf(currentPost.getUserId()));
        postViewHolder.mDisplayPostId.setText(String.valueOf(currentPost.getId()));
        postViewHolder.mDisplayTitle.setText(currentPost.getTitle());
        postViewHolder.mDisplayBody.setText(currentPost.getBody());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_user_id)
        TextView mDisplayUserId;

        @BindView(R.id.text_post_id)
        TextView mDisplayPostId;

        @BindView(R.id.text_title)
        TextView mDisplayTitle;

        @BindView(R.id.text_body)
        TextView mDisplayBody;

        public PostViewHolder(@NonNull View postView) {
            super(postView);
            ButterKnife.bind(this, postView);
        }
    }
}
