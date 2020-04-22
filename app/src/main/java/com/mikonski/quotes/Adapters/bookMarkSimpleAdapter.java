package com.mikonski.quotes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikonski.quotes.Models.BookMark;
import com.mikonski.quotes.R;

import java.util.List;

public class bookMarkSimpleAdapter extends RecyclerView.Adapter<bookMarkSimpleAdapter.bookmarkholder>{
    private List<BookMark> bookMarkList;
    private Context context;

    public bookMarkSimpleAdapter(List<BookMark> bookMarkList, Context context) {
        this.bookMarkList = bookMarkList;
        this.context = context;
    }

    @NonNull
    @Override
    public bookmarkholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_single_item,parent,false);
        return new bookmarkholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookmarkholder holder, int position) {

        BookMark bookMark = bookMarkList.get(position);

        if(position == 0){
            holder.title.setText(bookMark.getTitle());
            holder.title.setTextColor(context.getResources().getColor(R.color.colorAccent));

        }else if(position == 1){
            holder.title.setText(bookMark.getTitle());
            holder.number.setText("0 prayers");
            holder.title.setTextColor(context.getResources().getColor(R.color.colorBlue));

        }
        else if(position==2){
            holder.title.setText(bookMark.getTitle());
            holder.number.setText("0 devotions");
            holder.title.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }
        else {
            holder.title.setText(bookMark.getTitle());
            holder.number.setText("0 requests");
            holder.title.setTextColor(context.getResources().getColor(R.color.colorOrange));
        }

    }

    @Override
    public int getItemCount() {
        return bookMarkList.size();
    }

    public class bookmarkholder extends RecyclerView.ViewHolder{
        TextView title, number, date;

        public bookmarkholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_title);
            number = itemView.findViewById(R.id.text_number);
            date = itemView.findViewById(R.id.text_date);

        }
    }
}
