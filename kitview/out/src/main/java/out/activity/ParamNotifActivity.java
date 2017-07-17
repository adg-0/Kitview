package out.activity;

/**
 * Created by orthalis on 07/06/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kitview.out.mobile.R;

import java.util.HashMap;
import java.util.Map;

import util.app.AppConfig;
import util.app.AppController;
import util.session.SQLiteHandler;
import util.session.SessionManager;

public class ParamNotifActivity extends AppCompatActivity {
    Button button;
    SQLiteHandler db;
    SessionManager session;

    String tag_string_req = "req_param_notif_activity";

    private TextView txtNom;
    private TextView txtPrenom;


    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private RadioGroup radioGroupFamille;
    private RadioButton radioButtonFamille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.param_notif);

        txtNom = (TextView) findViewById(R.id.nom);
        txtPrenom = (TextView) findViewById(R.id.prenom);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        final String uid = user.get("uid");
        final String nom = user.get("nom");
        final String prenom = user.get("prenom");


        // Displaying the user details on the screen
        txtNom.setText(nom);
        txtPrenom.setText(prenom);

        //addListenerOnButton();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroupFamille = (RadioGroup) findViewById(R.id.radioGroupFamille);
        button = (Button) findViewById(R.id.notifButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                int selectedIdFamille = radioGroupFamille.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButtonFamille = (RadioButton) findViewById(selectedIdFamille);
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                //token
                String ff = FirebaseInstanceId.getInstance().getToken();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),ff);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("fcm_token", token);
                        params.put("rdv_notif_delta",(String) radioButton.getText());
                        params.put("patient_id", uid);
                        params.put("first_name", prenom);
                        params.put("last_name", nom);
                        params.put("notif_famille",transformeNotifFamille((String) radioButtonFamille.getText()));

                        return params;


                    }
                };


                    AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
                // Launch main activity
                Intent intent = new Intent(ParamNotifActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private String transformeNotifFamille (String reponse) {
        //permet de transformer la réponse Oui pas Y ou Non pas N pour l'envoyer à la BDD
        if (reponse.equals("Oui")) {
            return "Y";
        } else {
            return "N";
        }
    }

    //si on vient plusieurs fois sur cette activité, enlever ça
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
