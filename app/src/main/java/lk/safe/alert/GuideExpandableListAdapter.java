package lk.safe.alert;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class GuideExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> guideCategories;
    private HashMap<String, List<OfflineGuideActivity.GuideItem>> guideItems;

    public GuideExpandableListAdapter(Context context, List<String> guideCategories,
                                      HashMap<String, List<OfflineGuideActivity.GuideItem>> guideItems) {
        this.context = context;
        this.guideCategories = guideCategories;
        this.guideItems = guideItems;
    }

    @Override
    public int getGroupCount() {
        return guideCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<OfflineGuideActivity.GuideItem> items = guideItems.get(guideCategories.get(groupPosition));
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return guideCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<OfflineGuideActivity.GuideItem> items = guideItems.get(guideCategories.get(groupPosition));
        return items != null ? items.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String categoryTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }

        TextView categoryTextView = convertView.findViewById(android.R.id.text1);
        categoryTextView.setText(categoryTitle);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        categoryTextView.setTextSize(16);
        categoryTextView.setPadding(50, 30, 20, 30);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        OfflineGuideActivity.GuideItem guideItem = (OfflineGuideActivity.GuideItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView titleTextView = convertView.findViewById(android.R.id.text1);
        TextView descTextView = convertView.findViewById(android.R.id.text2);

        if (guideItem != null) {
            titleTextView.setText(guideItem.getTitle());
            descTextView.setText(guideItem.getDescription());
        }

        titleTextView.setTextSize(14);
        descTextView.setTextSize(12);
        titleTextView.setPadding(80, 20, 20, 10);
        descTextView.setPadding(80, 0, 20, 20);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}