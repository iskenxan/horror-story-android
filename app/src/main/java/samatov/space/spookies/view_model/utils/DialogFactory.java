package samatov.space.spookies.view_model.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import samatov.space.spookies.R;

public class DialogFactory {


    public static DialogPlus getDialogPlus(Context context, boolean expanded, int view, int gravity) {
        return DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(view))
                .setExpanded(expanded)
                .setGravity(gravity)
                .create();
    }


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
        SweetAlertDialog dialog = getInformationDialog(activity,
                title, text, showCancel, listener, SweetAlertDialog.WARNING_TYPE);
        return dialog;
    }


    public static SweetAlertDialog getNormalDialog(AppCompatActivity activity, String title,
                                                  String text, boolean showCancel, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog dialog = getInformationDialog(activity, title,
                text, showCancel, listener, SweetAlertDialog.NORMAL_TYPE);

        return dialog;
    }


    private static SweetAlertDialog getInformationDialog(AppCompatActivity activity, String title,
                                                         String text, boolean showCancel, SweetAlertDialog.OnSweetClickListener listener, int type) {
        SweetAlertDialog dialog = new SweetAlertDialog(activity, type);

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
