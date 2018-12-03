package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import samatov.space.spookies.R;


public class PostsListFragment extends Fragment {

    public PostsListFragment() {

    }


    public static PostsListFragment newInstance() {
        PostsListFragment fragment = new PostsListFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
