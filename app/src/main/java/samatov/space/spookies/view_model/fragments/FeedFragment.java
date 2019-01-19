package samatov.space.spookies.view_model.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.view_model.activities.FeedActivity;
import samatov.space.spookies.view_model.fragments.feed_viewpager.FeedViewPagerAdapter;


public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor
    }


    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }


    @BindView(R.id.feedViewPager) ViewPager mViewPager;

    FeedViewPagerAdapter mAdapter;
    List<Post> mTimeline = new ArrayList<>();
    List<Post> mPopular = new ArrayList<>();
    FeedActivity mActivity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);
        mActivity = (FeedActivity) getActivity();
//        getFeed();
        setupViewPager();

       return view;
    }


    //TODO: get this lists and pass on from FeedActivity, when creating new activity in the stream add the author of the post
    //TODO: test the view pager for the feed and it's views
    
    public void getFeed() {
        mTimeline = MyPreferenceManager
                .getListOfObjects(getContext(), MyPreferenceManager.FEED_TIMELINE, Post.class);
        mPopular = MyPreferenceManager
                .getListOfObjects(getContext(), MyPreferenceManager.FEED_POPULAR, Post.class);
    }


    public void setupViewPager() {
        SmartTabLayout tabs = mActivity.getViewPagerTabs();
        FragmentManager fm = getChildFragmentManager();
        mAdapter = new FeedViewPagerAdapter(fm, mTimeline, mPopular);
        mViewPager.setAdapter(mAdapter);
        tabs.setViewPager(mViewPager);
    }
}
