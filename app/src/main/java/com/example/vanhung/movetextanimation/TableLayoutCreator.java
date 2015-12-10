package com.example.vanhung.movetextanimation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by vanhung on 11/27/2015.
 */
public class TableLayoutCreator {

    public static TableLayout createView(Context context)
    {
        TableLayout result=new TableLayout(context);
        TableRow row=new TableRow(context);
        TableRow.LayoutParams params=new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.span=3;
        row.setLayoutParams(params);
        result.addView(row);
        return result;
    }
}
