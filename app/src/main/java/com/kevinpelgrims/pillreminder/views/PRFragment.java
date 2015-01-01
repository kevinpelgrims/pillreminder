package com.kevinpelgrims.pillreminder.views;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class PRFragment extends Fragment {
    protected OnPRFragmentInteractionListener onPRFragmentInteractionListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPRFragmentInteractionListener = (OnPRFragmentInteractionListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPRFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPRFragmentInteractionListener = null;
    }

    public interface OnPRFragmentInteractionListener {
        void OnFragmentCloseRequest();
    }
}
