package org.icddrb.listinggps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;


public class SettingForm extends Activity {
    Connection C;
    Global g;
    //private ProgressDialog dialog;

    String Site   = "";
    String UserID = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.devicesetting);
            C = new Connection(this);
            g = Global.getInstance();

            if (!Connection.haveNetworkConnection(SettingForm.this)) {
                Connection.MessageBox(SettingForm.this, "Inernet connection is not available for device setting.");
                return;
            }

            final Spinner spnUser = (Spinner)findViewById(R.id.spnUser);
            SpinnerItem(spnUser, "select UserId+'-'+UserName from UserList order by UserId");

            Button cmdSave = (Button)findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        String SQLStr   = "";

                        String[] User = spnUser.getSelectedItem().toString().split("-");
                        UserID = User[0];

                        String Setting = C.ReturnResult("Existence", "Select UserId from UserList where UserId='"+ Connection.SelectedSpinnerValue(spnUser.getSelectedItem().toString(),"-") +"' and Setting='1'");
                        if (Setting.equals("2")) {
                            Connection.MessageBox(SettingForm.this, "Device ID :"+ spnUser.getSelectedItem().toString() +" is not allowed to configure a mobile device, contact with administrator.");
                            return;
                        }

                        String ResponseString="Status:";

                        final ProgressDialog progDailog = ProgressDialog.show(SettingForm.this, "", "Please Wait . . .", true);

                        new Thread() {
                            public void run() {
                                try {
                                    C.RebuildDatabase(Site, UserID);
                                } catch (Exception e) {

                                }
                                progDailog.dismiss();

                                //Call Login Form
                                finish();
                                Intent f1 = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(f1);

                            }
                        }.start();
                    }
                    catch(Exception ex)
                    {
                        Connection.MessageBox(SettingForm.this, ex.getMessage());
                        return;
                    }
                }
            });
        }
        catch(Exception ex)
        {
            Connection.MessageBox(SettingForm.this, ex.getMessage());
            return;
        }
    }

    private void SpinnerItem(Spinner SpinnerName, String SQL)
    {
        List<String> listItem = new ArrayList<String>();
        listItem = C.DataListJSON(SQL);
        ArrayAdapter<String> adptrList= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItem);
        SpinnerName.setAdapter(adptrList);
    }
}