package com.example.jinwoo.lab3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * NameAdapter adapter som ärver från BaseAdapter och skapar
 * items (i vårt fall namn i Strings) av klassen "NameList".
 *
 * @author Jinwoo Yu
 * @version 2016.05.30
 */
public class NameAdapter extends BaseAdapter{
    Context context;
    private List<String> namesList;

    /**
     * Konstruktor som tar emot "context" och listan med alla namnförslag.
     * @param theContext Context.
     * @param theNamesList Listan av String med alla namnförslag.
     */
    public NameAdapter(Context theContext, List<String> theNamesList){
        context = theContext;
        namesList = theNamesList;
    }

    /**
     * Ta storleken på listan med alla namnförslag.
     * @return Antal namn som finns i listan med alla namnförslag.
     */
    @Override
    public int getCount(){
        if(namesList == null) return 0;
        return namesList.size();
    }

    /**
     * Skapar instanser av NameList.
     * @param index Sökindex.
     * @param v Vyn.
     * @param viewGroup
     * @return En instans av "NameList".
     */
    @Override
    public View getView(int index, View v, ViewGroup viewGroup){return new NameList(context, namesList.get(index));}

    @Override
    public long getItemId(int position) {return 0;} // Behövs annars så måste klassen vara abstrakt.

    @Override
    public Object getItem(int position) {return namesList.get(position);}
}
