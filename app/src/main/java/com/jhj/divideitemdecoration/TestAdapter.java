package com.jhj.divideitemdecoration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jianhaojie on 2017/6/20.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ItemViewHolder> {
    private final Context mContext;
    private List<Bean> mDatas;
    private LayoutInflater mInflater;

    TestAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    TestAdapter setDatas(List<Bean> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_city, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Bean bean = mDatas.get(position);
        holder.tvCity.setText(bean.getName());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, bean.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.avatar.setImageResource(R.drawable.friend);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        ImageView avatar;
        View content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
        }
    }
}
