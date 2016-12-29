package org.icddrb.listinggps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;

public class MainMenu extends Activity {

    static String USERID = "";
    Button cmdDataUpload;
    Button cmdDataSync;
    Connection C;
    Global g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //getLayoutInflater().inflate(R.layout.main_menu, frameLayout);
            setContentView(R.layout.main_menu);
            C = new Connection(this);
            g = Global.getInstance();

            USERID = g.getUserId();

            cmdDataSync = (Button) findViewById(R.id.cmdDataSync);
            cmdDataSync.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //Check for Internet connectivity
                    if (Connection.haveNetworkConnection(MainMenu.this)) {
                    } else {
                        Connection.MessageBox(MainMenu.this, "Internet connection is not available for Data Sync.");
                        return;
                    }

                    AlertDialog.Builder adb = new AlertDialog.Builder(MainMenu.this);
                    adb.setTitle("Data Sync");
                    adb.setMessage("Do you want to Sync Data[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            C = new Connection(MainMenu.this);
                            final ProgressDialog progDailog = ProgressDialog.show(MainMenu.this, "", "Please Wait . . .", true);

                            new Thread() {
                                public void run() {
                                    try {

                                        List<String> tableList = new ArrayList<String>();
                                        tableList.add("Screening");
                                        tableList.add("idnHistory");
                                        tableList.add("medRecord");
                                        tableList.add("Admission");
                                        tableList.add("Folup");
                                        tableList.add("Medicine");
                                        tableList.add("OthInvestig");

                                        //Lab
                                        tableList.add("SampleAnalysis");
                                        tableList.add("LabResult");

                                        C.DataSync_UploadDownload(tableList, USERID);

                                    } catch (Exception e) {

                                    }
                                    progDailog.dismiss();
                                }
                            }.start();

                        }
                    });
                    adb.show();
                }
            });

            Button cmdExit = (Button) findViewById(R.id.cmdExit);
            cmdExit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainMenu.this);
                    adb.setTitle("Exit");
                    adb.setMessage("Do you want to exit from the system[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adb.show();
                }
            });



            /* Start Usage of SharedPreferences class */
            /*
            final MySharedPreferences mySharedPreferences = new MySharedPreferences();
            final EditText txtSPrefInput = (EditText) findViewById(R.id.txtSPrefInput);
            final EditText txtSPrefOutput = (EditText) findViewById(R.id.txtSPrefOutput);
            Button cmdSPrefSave = (Button) findViewById(R.id.cmdSPrefSave);
            cmdSPrefSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySharedPreferences.save(MainMenu.this, "KeyName", txtSPrefInput.getText().toString());
                }
            });
            Button cmdSPrefGet = (Button) findViewById(R.id.cmdSPrefGet);
            cmdSPrefGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = mySharedPreferences.getValue(MainMenu.this, "KeyName");
                    txtSPrefOutput.setText(value);
                }
            });
            Button cmdSPrefRemove = (Button) findViewById(R.id.cmdSPrefRemove);
            cmdSPrefRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySharedPreferences.removeValue(MainMenu.this, "KeyName");
                    txtSPrefOutput.setText("Removed");
                }
            });
            Button cmdSPrefClear = (Button) findViewById(R.id.cmdSPrefClear);
            cmdSPrefClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mySharedPreferences.clearSharedPreference(MainMenu.this);
                }
            });
            */
            /* End Usage of SharedPreferences class */


        } catch (Exception ex)
        {
            Connection.MessageBox(MainMenu.this,ex.getMessage());
        }
    }
}
