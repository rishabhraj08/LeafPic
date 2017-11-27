package org.horaapps.leafpic.FaceRecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.horaapps.leafpic.R;
import org.horaapps.liz.ThemedActivity;
import org.json.*;
import com.kairos.*;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
public class FaceRecog extends ThemedActivity {

    TextView Text;


    Intent xx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_recog);

        xx=getIntent();
        String uristring=xx.getStringExtra("uri");
        Text=(TextView)findViewById(R.id.txt);
        KairosListener listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                //Log.d("KAIROS DEMO", response);
                //Text.setText(response);
                int spaces=2;
                String pretty_json="";
                try
                {
                   pretty_json= new JSONObject(response).toString(spaces);
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
                Text.setText(pretty_json);
                try{
                    JSONObject obj = new JSONObject(response);
                    String n=obj.getString("images");
                    Log.d("sample",n);
                    JSONArray jArray=new JSONArray(n);
                    for(int i=0;i<(jArray.length());i++)
                    {
                        JSONObject json_obj=jArray.getJSONObject(i);
                        String faces=json_obj.getString("faces");
                        Log.d("faces are ",faces);
                        JSONArray jsonArray2=new JSONArray(faces);
                        for(int j=0;j<(jsonArray2.length());j++)
                        {
                            JSONObject json_faces=jsonArray2.getJSONObject(j);
                            String chin=json_faces.getString("chinTipX");
                            chin=chin+' '+json_faces.getString("chinTipY");
                            Log.d("Coordinates of chin ",chin);
                            String eyeDistance=json_faces.getString("eyeDistance");
                            Log.d("eyeDistance ",eyeDistance);
                            String attributes=json_faces.getString("attributes");
                            Log.d("attributes are: ",attributes);
                            JSONObject attribute=new JSONObject(attributes);
                            Log.d("how much asian : ",attribute.getString("asian"));
                            JSONObject gender=new JSONObject(attribute.getString("gender"));
                            Log.d("Type ",gender.getString("type"));
                        }
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();}


            }

            @Override
            public void onFail(String response) {
                Log.d("KAIROS DEMO", response);

            }
        };
         /* * * instantiate a new kairos instance * * */
        Kairos myKairos = new Kairos();

        /* * * set authentication * * */
        String app_id = "66438bfa";
        String api_key = "a1d1e8dfbd8b207c838a2f57d028ccd9";
        myKairos.setAuthentication(this, app_id, api_key);
        String image = "http://media.kairos.com/liz.jpg";

        try {
            myKairos.detect(uristring, null, null, listener);
            //myKairos.enroll(image, "Elizabeth", "friends", null, null, null, listener);
            //myKairos.listGalleries(listener);
            //myKairos.listSubjectsForGallery("friends", listener);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
