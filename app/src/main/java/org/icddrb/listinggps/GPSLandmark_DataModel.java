package org.icddrb.listinggps;

import android.content.Context;
import android.database.Cursor;
import Common.Connection;
import java.util.ArrayList;
import java.util.List;
public class GPSLandmark_DataModel{

    private String _ProjId = "";
    public String getProjId(){
        return _ProjId;
    }
    public void setProjId(String newValue){
        _ProjId = newValue;
    }
    private String _VCode = "";
    public String getVCode(){
        return _VCode;
    }
    public void setVCode(String newValue){
        _VCode = newValue;
    }
    private String _ParaName = "";
    public String getParaName(){
        return _ParaName;
    }
    public void setParaName(String newValue){
        _ParaName = newValue;
    }
    private String _LMNo = "";
    public String getLMNo(){
        return _LMNo;
    }
    public void setLMNo(String newValue){
        _LMNo = newValue;
    }
    private String _LMName = "";
    public String getLMName(){
        return _LMName;
    }
    public void setLMName(String newValue){
        _LMName = newValue;
    }
    private String _latDeg = "";
    public String getlatDeg(){
        return _latDeg;
    }
    public void setlatDeg(String newValue){
        _latDeg = newValue;
    }
    private String _latMin = "";
    public String getlatMin(){
        return _latMin;
    }
    public void setlatMin(String newValue){
        _latMin = newValue;
    }
    private String _latSec = "";
    public String getlatSec(){
        return _latSec;
    }
    public void setlatSec(String newValue){
        _latSec = newValue;
    }
    private String _lonDeg = "";
    public String getlonDeg(){
        return _lonDeg;
    }
    public void setlonDeg(String newValue){
        _lonDeg = newValue;
    }
    private String _lonMin = "";
    public String getlonMin(){
        return _lonMin;
    }
    public void setlonMin(String newValue){
        _lonMin = newValue;
    }
    private String _lonSec = "";
    public String getlonSec(){
        return _lonSec;
    }
    public void setlonSec(String newValue){
        _lonSec = newValue;
    }
    private String _StartTime = "";
    public void setStartTime(String newValue){
        _StartTime = newValue;
    }
    private String _EndTime = "";
    public void setEndTime(String newValue){
        _EndTime = newValue;
    }
    private String _UserId = "";
    public void setUserId(String newValue){
        _UserId = newValue;
    }
    private String _EntryUser = "";
    public void setEntryUser(String newValue){
        _EntryUser = newValue;
    }
    private String _Lat = "";
    public void setLat(String newValue){
        _Lat = newValue;
    }
    private String _Lon = "";
    public void setLon(String newValue){
        _Lon = newValue;
    }
    private String _EnDt = "";
    public void setEnDt(String newValue){
        _EnDt = newValue;
    }
    private String _Upload = "2";

    String TableName = "GPSLandmark";

    public String SaveUpdateData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            if(C.Existence("Select * from "+ TableName +"  Where ProjId='"+ _ProjId +"' and VCode='"+ _VCode +"' and LMNo='"+ _LMNo +"' "))
                response = UpdateData(context);
            else
                response = SaveData(context);
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }
    Connection C;

    private String SaveData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            SQL = "Insert into "+ TableName +" (ProjId,VCode,ParaName,LMNo,LMName,latDeg,latMin,latSec,lonDeg,lonMin,lonSec,StartTime,EndTime,UserId,EntryUser,Lat,Lon,EnDt,Upload)Values('"+ _ProjId +"', '"+ _VCode +"', '"+ _ParaName +"', '"+ _LMNo +"', '"+ _LMName +"', '"+ _latDeg +"', '"+ _latMin +"', '"+ _latSec +"', '"+ _lonDeg +"', '"+ _lonMin +"', '"+ _lonSec +"', '"+ _StartTime +"', '"+ _EndTime +"', '"+ _UserId +"', '"+ _EntryUser +"', '"+ _Lat +"', '"+ _Lon +"', '"+ _EnDt +"', '"+ _Upload +"')";
            C.Save(SQL);
            C.close();
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }

    private String UpdateData(Context context)
    {
        String response = "";
        C = new Connection(context);
        String SQL = "";
        try
        {
            SQL = "Update "+ TableName +" Set Upload='2',ProjId = '"+ _ProjId +"',VCode = '"+ _VCode +"',ParaName = '"+ _ParaName +"',LMNo = '"+ _LMNo +"',LMName = '"+ _LMName +"',latDeg = '"+ _latDeg +"',latMin = '"+ _latMin +"',latSec = '"+ _latSec +"',lonDeg = '"+ _lonDeg +"',lonMin = '"+ _lonMin +"',lonSec = '"+ _lonSec +"'  Where ProjId='"+ _ProjId +"' and VCode='"+ _VCode +"' and LMNo='"+ _LMNo +"'";
            C.Save(SQL);
            C.close();
        }
        catch(Exception  e)
        {
            response = e.getMessage();
        }
        return response;
    }


    public List<GPSLandmark_DataModel> SelectAll(Context context, String SQL)
    {
        Connection C = new Connection(context);
        List<GPSLandmark_DataModel> data = new ArrayList<GPSLandmark_DataModel>();
        GPSLandmark_DataModel d = new GPSLandmark_DataModel();
        Cursor cur = C.ReadData(SQL);

        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            d = new GPSLandmark_DataModel();
            d._ProjId = cur.getString(cur.getColumnIndex("ProjId"));
            d._VCode = cur.getString(cur.getColumnIndex("VCode"));
            d._ParaName = cur.getString(cur.getColumnIndex("ParaName"));
            d._LMNo = cur.getString(cur.getColumnIndex("LMNo"));
            d._LMName = cur.getString(cur.getColumnIndex("LMName"));
            d._latDeg = cur.getString(cur.getColumnIndex("latDeg"));
            d._latMin = cur.getString(cur.getColumnIndex("latMin"));
            d._latSec = cur.getString(cur.getColumnIndex("latSec"));
            d._lonDeg = cur.getString(cur.getColumnIndex("lonDeg"));
            d._lonMin = cur.getString(cur.getColumnIndex("lonMin"));
            d._lonSec = cur.getString(cur.getColumnIndex("lonSec"));
            data.add(d);

            cur.moveToNext();
        }
        cur.close();
        return data;
    }
}

