package com.rumahyatimindonesia.ryi;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
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
 * Created by wahyudhzt on 4/19/2016.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuList> menuList;

    public MenuAdapter(Context context, List<MenuList> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        holder.Menu_Title.setText(menuList.get(position).getTitle());
        holder.Menu_Image.setImageResource(menuList.get(position).getImage());
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.menu_list, viewGroup, false);

        return new MenuViewHolder(itemView);
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        protected TextView Menu_Title;
        protected ImageView Menu_Image;

        public MenuViewHolder(View v) {
            super(v);
            Menu_Title =  (TextView) v.findViewById(R.id.menu_title);
            Menu_Image = (ImageView) v.findViewById(R.id.menu_image);
        }

    }
}
