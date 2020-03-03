package ir.technopedia.covino;

import android.support.v4.app.Fragment;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class BaseFragment extends Fragment {

    public void showToast(String message,int faz) {
        if(faz==0)//warning
        Crouton.makeText(getActivity(), message, Style.ALERT).show();
        else
            Crouton.makeText(getActivity(), message, Style.CONFIRM).show();


    }
}
