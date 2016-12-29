package org.icddrb.listinggps;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;

import Common.Connection;

/*
 * Created by TanvirHossain on 08/03/2015.
 */
public class DataSyncService extends Service
{
    Connection C;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*@Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        C=new Connection(this);
    }*/

    private void handleIntent(Intent intent) {
        // obtain the wake lock
        /*
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Const.TAG);
                mWakeLock.acquire();
        */

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        C = new Connection(this);
        // do the actual work, in a separate thread
        new DataSyncTask().execute();
    }

    //@SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //mWakeLock.release();
    }

    private class DataSyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                new Thread() {
                    public void run() {
                        try {


                            //Download file from server
                            String fileName = "village.txt";
                            Connection.ExecuteSQLFromFile(fileName);

                            /*DownloadTextFile d = new DownloadTextFile();
                            d.setContext(getApplicationContext());
                            String fileURL   = ProjectSetting.Namespace + "/"+ ProjectSetting.apiName +"/Update/"+ fileName;
                            d.execute(fileURL, fileName);*/

                        } catch (Exception e) {

                        }
                    }
                }.start();
                //Sync Database

            } catch (Exception e) {

            }

            // do stuff!
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // handle your data
            stopSelf();
        }
    }

}

