package samatov.space.spookies.view_model.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import samatov.space.spookies.R;

public class EditPostFragment extends Fragment {

    public EditPostFragment() {
        // Required empty public constructor
    }


    public static EditPostFragment newInstance() {
        EditPostFragment fragment = new EditPostFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

       return view;
    }

}
