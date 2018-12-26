package samatov.space.spookies.view_model.fragments.my_profile;

import android.Manifest;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import pl.aprilapps.easyphotopicker.EasyImage;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class GalleryPickerHandler {

    AppCompatActivity mActivity;
    View mView;


    public GalleryPickerHandler(AppCompatActivity activity, View contentView) {
        mActivity = activity;
        mView = contentView;
    }


    public void requestPermission() {
        Dexter.withActivity(mActivity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(getPermissionListener()).check();
    }


    public PermissionListener getPermissionListener() {
        return new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                startImagePicker();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                showPermissionDeniedSnackbar();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                displayPermissionAlertDialog(token);
            }
        };
    }


    private void startImagePicker() {
        if (EasyImage.canDeviceHandleGallery(mActivity))
            EasyImage.openGallery(mActivity, 0);
        else
            EasyImage.openDocuments(mActivity, 0);
    }


    private void showPermissionDeniedSnackbar() {
        String message = "We need an access to your files to pick the profile image";
        Snackbar snapckbar = Snackbar.make(mView, message, Snackbar.LENGTH_LONG);
        snapckbar.setAction("Allow", event -> requestPermission());
        snapckbar.show();
    }


    private void displayPermissionAlertDialog(PermissionToken token) {
        DialogFactory.getAlertDialog(mActivity, "Access required", "We need access to your files to pick profile image",
                false, dialog -> {
                    dialog.dismiss();
                    token.continuePermissionRequest();
                }).show();
    }
}
