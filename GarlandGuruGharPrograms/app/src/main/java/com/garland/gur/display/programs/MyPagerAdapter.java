package com.garland.gur.display.programs;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.garland.gur.display.programs.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    private int[] layouts;
    private List<TVShow> allTVShows=new ArrayList<>();

    private Context context;
    TableLayout country_table;

    public MyPagerAdapter(int[] layouts, List<TVShow> allTVShows, Context context) {
        this.layouts = layouts;
        this.context = context;
        this.allTVShows = allTVShows;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layouts[position], container, false);
        country_table=v.findViewById(R.id.country_table);
        fillCountryTable(position);

        container.addView(v);
        return v;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View)object;
        container.removeView(v);
    }

    private void fillCountryTable(int pagePosition) {

        for (int current = 0; current < allTVShows.size(); current++) {
            if (current == 0) {
                if (pagePosition == 0) {
                    setTableRowDetails("FRIDAY LANGAR", null, current);
                } else if (pagePosition == 1) {
                    setTableRowDetails("SUNDAY LANGAR", null, current);
                } else if (pagePosition == 2) {
                    setTableRowDetails("SPECIAL PROGRAMS", null, current);
                }
                setTableRowDetails("SEVA BY", "DATE", current);
            }
            boolean isValid = Boolean.FALSE;
            try {
                if (compareTheDates(allTVShows.get(current).getFullDt())) {
                    if (pagePosition == 0 && allTVShows.get(current).getDy().equalsIgnoreCase("FR")) {
                        isValid = Boolean.TRUE;
                    } else if (pagePosition == 1 && allTVShows.get(current).getDy().equalsIgnoreCase("SU")) {
                        isValid = Boolean.TRUE;
                    } else if (pagePosition == 2 && (allTVShows.get(current).getDy().equalsIgnoreCase("AK") ||
                            allTVShows.get(current).getDy().equalsIgnoreCase("SP"))) {
                        isValid = Boolean.TRUE;
                    }
                    if (isValid) {
                        setTableRowDetails(allTVShows.get(current).getTitle(),
                                allTVShows.get(current).getDy() + ", " + allTVShows.get(current).getDt(), current);
                    }
                }
            }catch (Exception e){

            }
        }
    }

    private void setTableRowDetails(String title, String dt, int current) {
        TableRow row = new TableRow(context);

        TextView t1 = new TextView(context);
        TextView t2 = new TextView(context);

        t1.setText(title);
        t1.setGravity(Gravity.CENTER);
        t1.setTextSize(40);
        t1.setTextColor(Color.parseColor("#FFFFFF"));
        if(dt==null) {
            t1.setBackgroundResource(R.color.colorPrimary);
            t1.setPaddingRelative(8, 8, 8, 8);
        }
        row.addView(t1);

        if(dt != null) {
            t2.setText(dt);
            t2.setGravity(Gravity.CENTER);
            t2.setTextSize(40);
            t2.setTextColor(Color.parseColor("#FFFFFF"));
            row.addView(t2);
        }else{
            TableRow.LayoutParams the_param= (TableRow.LayoutParams)row.getLayoutParams();
            if(null != the_param) {
                //t1.setTextColor(Color.parseColor("#B2c"));
                the_param.span = 2;
                row.setLayoutParams(the_param);
            }
        }

        if(null != country_table) {
            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tableRowParams.setMargins(3,3,3,3);
            tableRowParams.weight=0;
            country_table.addView(row, tableRowParams);

        }
    }

    public boolean compareTheDates(String date) throws ParseException {
        boolean op = Boolean.FALSE;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(date);


        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);

        System.out.println("date passed : " + date1 +" Today date ="+cal2.getTime());

        if (cal1.after(cal2)) {
            System.out.println("Date1 is after Date2");
            op = Boolean.TRUE;
        }

        if (cal1.before(cal2)) {
            System.out.println("Date1 is before Date2");
        }

        if (cal1.equals(cal2)) {
            System.out.println("Date1 is equal Date2");
            op = Boolean.TRUE;
        }
       return op;
    }



}
