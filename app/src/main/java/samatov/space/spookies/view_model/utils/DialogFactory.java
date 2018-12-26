package samatov.space.spookies.view_model.utils;

import android.support.v7.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import samatov.space.spookies.R;

public class DialogFactory {


    public static SweetAlertDialog getSuccessDialog(AppCompatActivity activity, String text,
                                                    SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        dialog.setTitle("Success!");
        dialog.setContentText(text);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(listener);
        dialog.setCancelable(false);

        return dialog;
    }


    public static SweetAlertDialog getLoadingDialog(AppCompatActivity activity, String text) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(activity.getResources().getColor(R.color.colorPrimary));
        dialog.setTitleText(text);
        dialog.setCancelable(false);

        return dialog;
    }


    public static SweetAlertDialog getErrorDialog(AppCompatActivity activity,
                                          String text, SweetAlertDialog.OnSweetClickListener listener) {

        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("Error!");
        dialog.setContentText(text);
        dialog.setCancelable(false);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(listener);

        return dialog;
    }


    public static SweetAlertDialog getAlertDialog(AppCompatActivity activity, String title, String text) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(title);
        dialog.setContentText(text);
        dialog.setCancelable(false);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(d -> d.dismiss());

        return dialog;
    }



    public static SweetAlertDialog getAlertDialog(AppCompatActivity activity, String title,
                                                  String text, boolean showCancel, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(title);
        dialog.setContentText(text);
        dialog.setCancelable(false);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(listener);
        if (showCancel) {
            dialog.setCancelText("Cancel");
            dialog.setCancelClickListener(d -> d.dismiss());
        }

        return dialog;
    }
}
