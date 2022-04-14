package com.example.thecomplaintbox.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecomplaintbox.AdminViewComplaint;
import com.example.thecomplaintbox.R;
import com.example.thecomplaintbox.UserViewComplaint;
import com.example.thecomplaintbox.model.User;
import com.example.thecomplaintbox.model.complainData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.viewHolder> {

    private int left_view=0;
    private int right_view=1;
    private ArrayList<HashMap<String,complainData>>muser;
    private ArrayList<String> keys;
    private Context mcontext;
    private String mproblemType;
    private String memail;
    private DatabaseReference databaseReference;


    public ComplaintAdapter(Context context, ArrayList<HashMap<String,complainData>> user,ArrayList<String> key,String problemType,String email) {
        mproblemType=problemType;
        memail=email;
        //Log.e("Adapter","error");

        muser=user;
        keys=key;
        mcontext=context;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==left_view){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.custom_left_chat,parent,false);
            return new viewHolder(view);
        }
        View view=LayoutInflater.from(mcontext).inflate(R.layout.custom_right_chat,parent,false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        HashMap<String,complainData> dd=muser.get(position);
        complainData cData=dd.get(keys.get(position));
        holder.data.setText(cData.getData());
        holder.email.setText(cData.getemail());


    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Log.e("getItemView","error");
        HashMap<String,complainData> dd=muser.get(position);
        complainData data=dd.get(keys.get(position));
        if(data.getemail().equals("Admin")){
            return left_view;
        }
        return right_view;

    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView email,data;

        public viewHolder( View itemView) {
            super(itemView);
            //Log.e("ViewHolder","error");

            email=itemView.findViewById(R.id.chatTextEmail);
            data=itemView.findViewById(R.id.chatText);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    databaseReference=FirebaseDatabase.getInstance().getReference("Complaints");
                    HashMap<String,complainData> hm=muser.get(getAdapterPosition());

                    complainData lData=hm.get(keys.get(getAdapterPosition()));
                    if(memail.equals(lData.getemail())){
                        complainData dd=new complainData(lData.getemail(),"Message Deleted");
                                databaseReference.child(UserViewComplaint.problemType).child(keys.get(getAdapterPosition()))
                                .setValue(dd)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(mcontext, "Message Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }

                                    }
                                });

                    }

                    else if(memail.equals("Admin")){
                            complainData dd=new complainData(lData.getemail(),"Message Deleted");
                                databaseReference.child(AdminViewComplaint.problemType).child(keys.get(getAdapterPosition()))
                                .setValue(dd)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(mcontext, "Message Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }

                                    }
                                });

                    }


                    return false;
                }
            });

        }
    }
}
