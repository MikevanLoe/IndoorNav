package project.movinindoor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Davey on 28-11-2014.
 */
public class ExpandableListAdapterNew extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapterNew(Context context, List<String> listDataHeader,
                                    HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageButton Button1= (ImageButton)  convertView.findViewById(R.id.imageButton);
        Button1.setTag(groupPosition);

        ImageButton Button2= (ImageButton)  convertView.findViewById(R.id.imageButton2);
        Button2.setTag(groupPosition);

        ImageButton Button3= (ImageButton)  convertView.findViewById(R.id.imageButton3);
        Button3.setTag(groupPosition);
        /*
        final String startRoom;

        if(groupPosition > 0) {
            startRoom = getChild(groupPosition - 1, 0).toString().substring(16);
        } else {
            startRoom = MapsActivity.editStart.getText().toString();
        }

        final String EndRoom = getChild(groupPosition, 0).toString().substring(16);


        ImageButton Button1= (ImageButton)  convertView.findViewById(R.id.imageButton);
        ImageButton Button2= (ImageButton)  convertView.findViewById(R.id.imageButton2);

        Button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MapsActivity.setupGraph.navigateRoute(startRoom, EndRoom);
                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Navigation started", Toast.LENGTH_SHORT).show();
            }

        });

        Button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Toast.makeText(MapsActivity.getContext().getApplicationContext(), "Repair repaired", Toast.LENGTH_SHORT).show();
            }

        });
        */

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}