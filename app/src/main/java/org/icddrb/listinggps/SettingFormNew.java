package org.icddrb.listinggps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Common.Connection;
import Common.Global;
import Common.ProjectSetting;


public class SettingFormNew extends Activity {
    Connection C;
    Global g;
    String Site = "";
    String UserID = "";
    int count = 0;
    //private ProgressDialog dialog;
    private ProgressDialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.devicesetting);
            C = new Connection(this);
            g = Global.getInstance();

            if (!Connection.haveNetworkConnection(SettingFormNew.this)) {
                Connection.MessageBox(SettingFormNew.this, "Inernet connection is not available for device setting.");
                return;
            }

            final Spinner spnUser = (Spinner) findViewById(R.id.spnUser);
            SpinnerItem(spnUser, "select UserId+'-'+UserName from UserList order by UserId");
            // SpinnerItem(spnUser, "Device-Test");

            Button cmdSave = (Button) findViewById(R.id.cmdSave);
            cmdSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        String SQLStr = "";

                        String[] User = spnUser.getSelectedItem().toString().split("-");
                        UserID = User[0];

                        String Setting = C.ReturnResult("Existence", "Select UserId from UserList where UserId='" + Connection.SelectedSpinnerValue(spnUser.getSelectedItem().toString(), "-") + "' and Setting='1'");
                        if (Setting.equals("2")) {
                            Connection.MessageBox(SettingFormNew.this, "Device ID :" + spnUser.getSelectedItem().toString() + " is not allowed to configure a mobile device, contact with administrator.");
                            return;
                        }

                        final String ResponseString = "Status:";

                        final ProgressDialog progDailog = ProgressDialog.show(SettingFormNew.this, "", "Please Wait . . .", true);

                        new Thread() {
                            public void run() {
                                try {
                                    setupUserId(UserID);
                                } catch (Exception e) {

                                }
                                progDailog.dismiss();


                            }
                        }.start();
                    } catch (Exception ex) {
                        Connection.MessageBox(SettingFormNew.this, ex.getMessage());
                        return;
                    }
                }
            });
        } catch (Exception ex) {
            Connection.MessageBox(SettingFormNew.this, ex.getMessage());
            return;
        }
    }


    private void downloadCommonDatabase() {

        CommonDBDownload commonDBDownload = new CommonDBDownload();
        commonDBDownload.setContext(getApplicationContext());
        commonDBDownload.execute(Global.CommonDB);

    }

    private void setupUserId(String userID) {
        while (!Connection.haveNetworkConnection(SettingFormNew.this)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingFormNew.this);
            dialog.setCancelable(false);
            dialog.setTitle("No Internet!");
            dialog.setMessage("Internet Connection required for setup!");
            dialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        downloadCommonDatabase();
      /*  String mSQL = "";
        File file = new File(Environment.getExternalStorageDirectory() +"/"+ Global.DatabaseFolder +"/"+ Global.DatabaseName);
        if (file.exists()) {
           // C.Save(mSQL);
            C.Sync_Download_Rebuild("UserList", "SiteCode='"+ Site +"' and UserId='"+ userID +"'");
        }
        else {
            setupUserId(userID);
        }*/
    }

    private void SpinnerItem(Spinner SpinnerName, String SQL) {
        List<String> listItem = new ArrayList<String>();
        listItem = C.DataListJSON(SQL);
        ArrayAdapter<String> adptrList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItem);
        SpinnerName.setAdapter(adptrList);
    }


    class CommonDBDownload extends AsyncTask<String, String, Void> {
        private Context context;


        public void setContext(Context contextf) {
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(SettingFormNew.this);
            dialog.setMessage(" Setup ongoing...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();*/
        }


        protected void onProgressUpdate(String... progress) {
            //   dialog.setProgress(Integer.parseInt(progress[0]));
            //publishProgress(progress);

        }


        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                //  c.setRequestMethod("GET");
                c.connect();
                int lenghtOfFile = c.getContentLength();

                File file = Environment.getExternalStorageDirectory();

                File mDatabase = new File(file.getAbsolutePath() + "/" + ProjectSetting.DatabaseFolder + "/" + ProjectSetting.DatabaseName);

                mDatabase.mkdir();
                if (mDatabase.exists()) {
                    mDatabase.delete();
                    mDatabase.createNewFile();
                } else {
                    mDatabase.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(mDatabase);

                InputStream is = c.getInputStream();


                byte[] buffer = new byte[1024];
                int len1 = 0;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    count++;
                }
                fos.close();
                is.close();

                //     dialog.dismiss();

            } catch (IOException e) {
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            File file = new File(Environment.getExternalStorageDirectory() + "/" + Global.DatabaseFolder + "/" + Global.DatabaseName);
            if (file.exists()) {
                C.Save("Delete from UserList");
                C.Sync_Download_Rebuild("UserList", "UserId='" + UserID + "'");
                C.ExecuteCommandOnServer("Update UserList set Setting='2' where UserId='" + UserID + "'");


            } else {
                setupUserId(UserID);
            }

            //Call Login Form
            finish();
            Intent f1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(f1);
        }
    }
}