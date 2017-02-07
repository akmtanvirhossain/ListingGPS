package org.icddrb.listinggps;


//Android Manifest Code
//<activity android:name=".GPSBari_list" android:label="GPSBari: List" />
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.view.KeyEvent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import Common.*;

public class GPSBari_list extends Activity {
    boolean networkAvailable=false;
    Location currentLocation;
    double currentLatitude,currentLongitude;
    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
    }
    String VariableID;
    private int hour;
    private int minute;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    SimpleAdapter dataAdapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static String TableName;

    TextView lblHeading;
    Button btnAdd;
    Button btnRefresh;

    //************added by sakib***************
    TextView tvUnion,tvVillage,tvListHeadingNo,tvListHeadingName,tvListHeadingPara;
    Spinner spnUnion,spnVillage;

    RadioGroup rdogrpBLD;

    RadioButton rdoBari,rdoLandmark,rdoDoctor;

    //************added by sakib***************

    String StartTime;
    static String PROJID = "0001";
    static String VCODE = "";
    static String BARINO = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.gpsbari_list);
            C = new Connection(this);
            g = Global.getInstance();
            StartTime = g.CurrentTime24();
            TableName = "GPSBari";

            //**************************added by sakib***************************
            tvUnion= (TextView) findViewById(R.id.tvUnion);
            tvVillage= (TextView) findViewById(R.id.tvVillage);

            spnUnion= (Spinner) findViewById(R.id.spnUnion);
            spnVillage= (Spinner) findViewById(R.id.spnVillage);

            rdogrpBLD= (RadioGroup) findViewById(R.id.rdogrpBLD);


            rdoBari= (RadioButton) findViewById(R.id.rdoBari);
            rdoBari.setChecked(true);

            tvListHeadingNo= (TextView) findViewById(R.id.tvListHeadingNo);
            tvListHeadingName= (TextView) findViewById(R.id.tvListHeadingName);
            tvListHeadingPara= (TextView) findViewById(R.id.tvListHeadingPara);


            rdoLandmark= (RadioButton) findViewById(R.id.rdoLandmark);
            rdoDoctor= (RadioButton) findViewById(R.id.rdoDoctor);
            //**************************added by sakib***************************

            lblHeading = (TextView)findViewById(R.id.lblHeading);
            lblHeading.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT  = 2;
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(event.getRawX() >= (lblHeading.getRight() - lblHeading.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            AlertDialog.Builder adb = new AlertDialog.Builder(GPSBari_list.this);
                            adb.setTitle("Close");
                            adb.setMessage("Do you want to close this form[Yes/No]?");
                            adb.setNegativeButton("No", null);
                            adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }});
                            adb.show();
                            return true;
                        }
                    }
                    return false;
                }
            });

            ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
            cmdBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(GPSBari_list.this);
                    adb.setTitle("Close");
                    adb.setMessage("Do you want to close this form[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }});
                    adb.show();
                }});

            btnRefresh = (Button) findViewById(R.id.btnRefresh);
            btnRefresh.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    //write your code here
                    if(rdoBari.isChecked()) DataSearch(PROJID, VCODE, BARINO,"1");
                    else if(rdoLandmark.isChecked()) DataSearch(PROJID, VCODE, BARINO,"2");
                    else if(rdoDoctor.isChecked()) DataSearch(PROJID, VCODE, BARINO,"3");

                }});

            btnAdd   = (Button) findViewById(R.id.btnAdd);
            btnAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if(spnUnion.getSelectedItemPosition()==0){
                        Toast.makeText(GPSBari_list.this, "Please Select Union", Toast.LENGTH_LONG).show();
                        return;
                    }else if(spnVillage.getSelectedItemPosition()==0)
                    {
                        Toast.makeText(GPSBari_list.this, "Please Select Village", Toast.LENGTH_LONG).show();
                        return;
                    }else if(!rdoBari.isChecked() & !rdoLandmark.isChecked() & !rdoDoctor.isChecked()){
                        Toast.makeText(GPSBari_list.this, "Please Select Bari/Landmark/Doctor", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    VCODE = C.ReturnSingleValue("Select VCode from Village where VName='"+ spnVillage.getSelectedItem() +"'");
                    Bundle IDbundle = new Bundle();
                    IDbundle.putString("ProjId", PROJID);
                    IDbundle.putString("VCode", VCODE);
                    IDbundle.putString("VName", (String) spnVillage.getSelectedItem());

                    String[] d_rdogrpElectricity = new String[]{"1", "2", "3"};

                    int rb = rdogrpBLD.getCheckedRadioButtonId();



                    Intent intent = null;

                    switch(rb)
                    {
                        case R.id.rdoBari:
                            //intent = new Intent(getApplicationContext(), GPSBari.class);
                            //IDbundle.putString("BariNo", "");
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //intent.putExtras(IDbundle);

                            intent = new Intent(getApplicationContext(), GPSBari.class);
                            IDbundle.putString("BariNo", "");
                            intent.putExtras(IDbundle);
                            startActivityForResult(intent, 1);

                            break;
                        case R.id.rdoLandmark:
                            /*intent = new Intent(getApplicationContext(), GPSLandmark.class);
                            IDbundle.putString("LMNo", "");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(IDbundle);*/

                            intent = new Intent(getApplicationContext(), GPSLandmark.class);
                            IDbundle.putString("LMNo", "");
                            intent.putExtras(IDbundle);
                            startActivityForResult(intent, 1);

                            break;
                        case R.id.rdoDoctor:
                            /*intent = new Intent(getApplicationContext(), GPSVDoctor.class);
                            IDbundle.putString("VDNo", "");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(IDbundle);*/

                            intent = new Intent(getApplicationContext(), GPSVDoctor.class);
                            IDbundle.putString("VDNo", "");
                            intent.putExtras(IDbundle);
                            startActivityForResult(intent, 1);

                            break;
                        default:
                            Toast.makeText(GPSBari_list.this, "Please Select Bari or Landmark or Doctor", Toast.LENGTH_SHORT).show();
                            break;
                    }


                }
            });


            //**************************added by sakib***************************


            //"Select '' Union Select distinct UnName||'-'||UnName from Village union Select '99-Others'"
            spnUnion.setAdapter( (C.getArrayAdapter("Select '' Union Select distinct UnName from Village")));
            spnUnion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (spnUnion.getSelectedItem().toString().length() == 0) return;
                    spnVillage.setAdapter(C.getArrayAdapter("Select '  ' Union  Select distinct VName from Village where UnName='"+ Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-")+"'"));

                    //spnVillage.setAdapter(C.getArrayAdapter("Select VName from Village where UnName='"+ Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-")+"'  union Select '999-Others'"));
                    if(Connection.SelectedSpinnerValue(spnUnion.getSelectedItem().toString(),"-").equals("999"))
                        spnVillage.setSelection(Global.SpinnerItemPositionAnyLength(spnVillage,"999"));
                    else
                        spnVillage.setSelection(Global.SpinnerItemPositionAnyLength(spnVillage,""));


                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });

            spnVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    VCODE = C.ReturnSingleValue("Select VCode from Village where VName='"+spnVillage.getSelectedItem()+"'");
                    int rb=rdogrpBLD.getCheckedRadioButtonId();
                    switch(rb)
                    {
                        case R.id.rdoBari:
                            DataSearch(PROJID, VCODE, BARINO,"1");

                            break;
                        case R.id.rdoLandmark:
                            DataSearch(PROJID, VCODE, BARINO,"2");

                            break;
                        case R.id.rdoDoctor:
                            DataSearch(PROJID, VCODE, BARINO,"3");

                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            rdogrpBLD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId)
                    {
                        case R.id.rdoBari:
                            tvListHeadingNo.setText("Bari No");
                            tvListHeadingName.setText("Bari Name");
                            tvListHeadingPara.setText("Para Name");
                            DataSearch(PROJID, VCODE, BARINO,"1");
                            btnAdd.setText("New "+rdoBari.getText());
                            break;
                        case R.id.rdoLandmark:
                            tvListHeadingNo.setText("Landmark No");
                            tvListHeadingName.setText("Landmark Name");
                            tvListHeadingPara.setText("Para Name");
                            DataSearch(PROJID, VCODE, BARINO,"2");
                            btnAdd.setText("New "+rdoLandmark.getText());
                            break;
                        case R.id.rdoDoctor:
                            tvListHeadingNo.setText("Doctor No");
                            tvListHeadingName.setText("Doctor Name");
                            tvListHeadingPara.setText("Para Name");
                            DataSearch(PROJID, VCODE, BARINO,"3");
                            btnAdd.setText("New "+rdoDoctor.getText());
                            break;
                    }
                }
            });
            //**************************added by sakib***************************



            DataSearch(PROJID, VCODE, BARINO,"1");


        }
        catch(Exception  e)
        {
            Connection.MessageBox(GPSBari_list.this, e.getMessage());
            return;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        } else {
            if(rdoBari.isChecked())
                DataSearch(PROJID, VCODE, BARINO,"1");
            else if(rdoLandmark.isChecked())
                DataSearch(PROJID, VCODE, BARINO,"2");
            else if(rdoDoctor.isChecked())
                DataSearch(PROJID, VCODE, BARINO,"3");

        }
    }


    private void DataSearch(String ProjId, String VCode, String BariNo,String Type)
    {
        try
        {
            String SQL = "";
            dataList.clear();

            dataAdapter = null;

            ListView list = (ListView)findViewById(R.id.lstData);
            HashMap<String, String> map;
            TextView tvTotal= (TextView) findViewById(R.id.tvTotal);

            if(Type.equals("1")) {
                SQL = "Select ProjId, VCode, ParaName, BariNo, WayPoint, BariName,TotalHH, latDeg, latMin, latSec, lonDeg, lonMin, lonSec, HCLoction, HCLoction1, HCLoction2, StartTime, EndTime, UserId, EntryUser, Lat, Lon, EnDt, Upload, modifyDate from GPSBari where VCode='"+VCode+"'";
                GPSBari_DataModel d = new GPSBari_DataModel();
                List<GPSBari_DataModel> data = d.SelectAll(this, SQL);
                for(GPSBari_DataModel item : data){
                    map = new HashMap<String, String>();
                    map.put("ProjId", item.getProjId());
                    map.put("VCode", item.getVCode());
                    map.put("ParaName", item.getParaName());
                    map.put("BariNo", item.getBariNo());
                    map.put("BariName", item.getBariName());
                    dataList.add(map);
                }

                //tvTotal.setText("Total:"+data.size());
                lblHeading.setText("GPS Listing (Total: "+ data.size()+")");

            }
            else if(Type.equals("2")) {
                SQL = "Select ProjId, VCode, ParaName, LMNo, WayPoint, LMName, latDeg, latMin, latSec, lonDeg, lonMin, lonSec, StartTime, EndTime, UserId, EntryUser, Lat, Lon, EnDt, Upload, modifyDate from GPSLandmark where VCode='"+VCode+"'";
                GPSLandmark_DataModel d = new GPSLandmark_DataModel();
                List<GPSLandmark_DataModel> data = d.SelectAll(this, SQL);
                for(GPSLandmark_DataModel item : data){
                    map = new HashMap<String, String>();
                    map.put("ProjId", item.getProjId());
                    map.put("VCode", item.getVCode());
                    map.put("ParaName", item.getParaName());
                    map.put("BariNo", item.getLMNo());
                    map.put("BariName", item.getLMName());
                    dataList.add(map);
                }
                lblHeading.setText("GPS Listing (Total: "+ data.size()+")");
            }
            else if(Type.equals("3")) {
                SQL = "Select ProjId, VCode, ParaName, HutBazar, VDNo ,WayPoint, VDName , VDType, VDTOther, PharName, latDeg, latMin, latSec, lonDeg, lonMin, lonSec, StartTime, EndTime, UserId, EntryUser, Lat, Lon, EnDt, Upload, modifyDate from GPSVDoctor where VCode='"+VCode+"'";
                GPSVDoctor_DataModel d = new GPSVDoctor_DataModel();
                List<GPSVDoctor_DataModel> data = d.SelectAll(this, SQL);
                for(GPSVDoctor_DataModel item : data){
                    map = new HashMap<String, String>();
                    map.put("ProjId", item.getProjId());
                    map.put("VCode", item.getVCode());
                    map.put("ParaName", item.getParaName());
                    map.put("BariNo", item.getVDNo());
                    map.put("BariName", item.getVDName());
                    dataList.add(map);
                }
                lblHeading.setText("GPS Listing (Total: "+ data.size()+")");
            }





            dataAdapter = new SimpleAdapter(GPSBari_list.this, dataList, R.layout.gpsbari_list,new String[] {"rowsec"},
                    new int[] {R.id.secListRow});
            list.setAdapter(new DataListAdapter(this, dataAdapter));
            Utility.setListViewHeightBasedOnChildren(list);
        }
        catch(Exception  e)
        {
            Connection.MessageBox(GPSBari_list.this, e.getMessage());
            return;
        }
    }


    public class DataListAdapter extends BaseAdapter
    {
        private Context context;
        private SimpleAdapter dataAdap;

        public DataListAdapter(Context c, SimpleAdapter da){ context = c;  dataAdap = da; }
        public int getCount() {  return dataAdap.getCount();  }
        public Object getItem(int position) {  return position;  }
        public long getItemId(int position) {  return position;  }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gpsbari_row, null);
            }
            LinearLayout   secListRow = (LinearLayout)convertView.findViewById(R.id.secListRow);

//            final TextView ProjId = (TextView)convertView.findViewById(R.id.ProjId);
//            final TextView VCode = (TextView)convertView.findViewById(R.id.VCode);
            final TextView ParaName = (TextView)convertView.findViewById(R.id.ParaName);
            final TextView BariNo = (TextView)convertView.findViewById(R.id.BariNo);
            final TextView BariName = (TextView)convertView.findViewById(R.id.BariName);


            final HashMap<String, String> o = (HashMap<String, String>) dataAdap.getItem(position);
//            ProjId.setText(o.get("ProjId"));
//            VCode.setText(o.get("VCode"));
            ParaName.setText(o.get("ParaName"));
            BariNo.setText(o.get("BariNo"));
            BariName.setText(o.get("BariName"));



            secListRow.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Write your code here
                    Bundle IDbundle = new Bundle();
                    int rb = rdogrpBLD.getCheckedRadioButtonId();
                    Intent f1=null;

                    switch(rb)
                    {
                        case R.id.rdoBari:
                            IDbundle.putString("ProjId", o.get("ProjId"));
                            IDbundle.putString("VCode", o.get("VCode"));
                            IDbundle.putString("BariNo", o.get("BariNo"));
                            IDbundle.putString("VName", (String)spnVillage.getSelectedItem());
                            f1 = new Intent(getApplicationContext(), GPSBari.class);
                            break;
                        case R.id.rdoLandmark:
                            IDbundle.putString("ProjId", o.get("ProjId"));
                            IDbundle.putString("VCode", o.get("VCode"));
                            IDbundle.putString("LMNo", o.get("BariNo"));
                            IDbundle.putString("VName",(String)spnVillage.getSelectedItem());
                            f1 = new Intent(getApplicationContext(), GPSLandmark.class);
                            break;
                        case R.id.rdoDoctor:
                            IDbundle.putString("ProjId", o.get("ProjId"));
                            IDbundle.putString("VCode", o.get("VCode"));
                            IDbundle.putString("VDNo", o.get("BariNo"));
                            IDbundle.putString("VName",(String)spnVillage.getSelectedItem());
                            f1 = new Intent(getApplicationContext(), GPSVDoctor.class);
                            break;
                        default:
                            Toast.makeText(GPSBari_list.this, "Please Select Bari or Landmark or Doctor", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    f1.putExtras(IDbundle);
                    startActivity(f1);
                }
            });


            return convertView;
        }
    }





}