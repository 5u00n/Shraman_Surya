package in.devedits.shramansurya.user.dashboardhelper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.devedits.shramansurya.R;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.viewHolder> {
    ArrayList<GridModels> list;
    Context context;


    //iew view;

    public DashboardRecyclerAdapter(ArrayList<GridModels> list, Context context) {
        // super();
        this.list = list;
        this.context = context;
       // this.view = view;
    }

    public void filterList(ArrayList<GridModels> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DashboardRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_grid_items, parent, false);
        return new DashboardRecyclerAdapter.viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DashboardRecyclerAdapter.viewHolder holder, int position) {
        GridModels model = list.get(position);


        holder.id.setText(model.getId());
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYY, hh:mm aa");

        holder.title.setText(model.getTitle());

        holder.icon.setImageResource(model.getIcon());

        holder.cardView.setCardBackgroundColor(Color.parseColor(model.getColor()));
        //holder.i.setText(model.getLife_span());
        ///Picasso.get().load(model.getImgurl()).into(holder.icon);
        //Picasso.get().load(model.user_img_url).into(holder.userimg);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,  model.getId(), Toast.LENGTH_SHORT).show();


                if (mListener != null) {
                   mListener.onItemClicked(model.getId()); // Pass the ID or relevant data

               }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView id, title;

        ImageView icon;

        CardView cardView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.grid_items_card);
            id = itemView.findViewById(R.id.grid_items_id);
            title = itemView.findViewById(R.id.grid_items_title);
            icon = itemView.findViewById(R.id.grid_items_icon);
        }
    }

    private DashboardRecyclerAdapter.OnItemClickListener mListener;

    public void setOnItemClickListener(DashboardRecyclerAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(String model);
    }
}
