package com.example.jinwoo.lab3;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Jinwoo on 2016-03-17.
 */
public class NameAdapter extends BaseAdapter{
    Context context;
    private List<String> namesList;

    public NameAdapter(Context theContext, List<String> theNamesList){
        context = theContext;
        namesList = theNamesList;
    }

    @Override
    public int getCount(){
        if(namesList == null) return 0;
        return namesList.size();
    }

    @Override
    public View getView(int index, View v, ViewGroup viewGroup){return new NameList(context, namesList.get(index));}

    @Override
    public long getItemId(int position) {return 0;} // Behövs annars så måste klassen vara abstrakt.

    @Override
    public Object getItem(int position)
    {
        return namesList.get(position);
    }
}
