package br.com.felixtec.oficinaappmobile;

/**
 * Created by Félix on 02/08/2016.
 */
/**
 * Created by Félix on 25/07/2016.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ExitDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private ExitListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ExitListener)) {
            throw new RuntimeException("A activity precisa implementar a interface ExitDialog.ExitListener");
        }

        listener = (ExitListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Deseja sair?")
                .setPositiveButton("Sim", this)
                .setNegativeButton("N\u00e3o", this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE && listener != null) {
            listener.onExit();
        }
    }

    public interface ExitListener {
        public void onExit();
    }
}

