package vn.edu.ecomapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogMessage {
    public static void showAlertMessage(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss()).setTitle(title).setMessage(message).show();
    }
}
