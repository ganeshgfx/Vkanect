package lit.de.vkanect;

import android.content.Context;
import android.widget.Toast;

public class Tools {
    void makeToast(Context context,String Text){
        Toast.makeText(context, Text, Toast.LENGTH_LONG).show();
    }
}
