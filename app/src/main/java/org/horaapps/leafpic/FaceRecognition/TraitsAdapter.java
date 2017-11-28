package org.horaapps.leafpic.FaceRecognition;

/**
 * Created by Sam on 11/28/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.horaapps.leafpic.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by hh on 11/27/2017.
 */

public class TraitsAdapter extends RecyclerView.Adapter<TraitsAdapter.MyViewHolder>
{

    Context con;
    View v;
    ArrayList<Traits>arr;
    public TraitsAdapter(Context con,ArrayList<Traits>arr)
    {
        this.con=con;
        this.arr=arr;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7,mTxt;
        ImageView img;
        public MyViewHolder(View view)
        {
            super(view);
            mTxt=(TextView)view.findViewById(R.id.cText);
            tx1=(TextView)view.findViewById(R.id.ctx2);
            tx2=(TextView)view.findViewById(R.id.ctx4);
            tx3=(TextView)view.findViewById(R.id.ctx6);
            tx4=(TextView)view.findViewById(R.id.ctx8);
            tx5=(TextView)view.findViewById(R.id.ctx10);
            tx6=(TextView)view.findViewById(R.id.ctx12);
            tx7=(TextView)view.findViewById(R.id.ctx14);
        }
    }

    @Override
    public TraitsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        v= LayoutInflater.from(con).inflate(R.layout.mycardview,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(TraitsAdapter.MyViewHolder holder, int position)
    {
        holder.tx1.setText(arr.get(position).getGender());
        holder.tx2.setText(arr.get(position).getAsian());
        holder.tx3.setText(arr.get(position).getEyeDistance());
        holder.tx4.setText(arr.get(position).getMaleConfidence());
        holder.tx5.setText(arr.get(position).getFemaleConfidence());
        holder.tx6.setText(arr.get(position).getAge());
        holder.tx7.setText(arr.get(position).getGlasses());
        holder.mTxt.setText("Face->"+position+1+"");
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }


}

