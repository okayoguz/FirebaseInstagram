package com.example.user.firebaseinstagram;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by user on 10.03.2018.
 */

public class PostClass2 extends ArrayAdapter<String> {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseUser chatuser = FirebaseAuth.getInstance().getCurrentUser();
    public String useremail = chatuser.getEmail();

    private final ArrayList<String> gonderici;
    private final ArrayList<String> alici;
    private final ArrayList<String> mesajText;
    private final Activity context;

    public PostClass2(ArrayList<String> cg, ArrayList<String> ca, ArrayList<String> cm, Activity context) {
        super(context,0,cg);
        this.gonderici = cg;
        this.alici = ca;
        this.mesajText = cm;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View customView;

        if(useremail.equals(this.gonderici.get(position))){

            LayoutInflater layoutInflater = context.getLayoutInflater();
            customView = layoutInflater.inflate(R.layout.costum_view2,null,true);

            TextView chatg = (TextView) customView.findViewById(R.id.gonderen);
            TextView chatm = (TextView) customView.findViewById(R.id.icerik);
            TextView chata = (TextView) customView.findViewById(R.id.alan);

            chatg.setText(gonderici.get(position));
            chatm.setText(mesajText.get(position));
            chata.setText(alici.get(position));
        }

        else{

            LayoutInflater layoutInflater = context.getLayoutInflater();
            customView = layoutInflater.inflate(R.layout.cosrum_view3,null,true);

            TextView chatg2 = (TextView) customView.findViewById(R.id.gonderen);
            TextView chatm2 = (TextView) customView.findViewById(R.id.icerik);
            TextView chata2 = (TextView) customView.findViewById(R.id.alan);

            chatg2.setText(gonderici.get(position));
            chatm2.setText(mesajText.get(position));
            chata2.setText(alici.get(position));

        }


        return customView;
    }
}
