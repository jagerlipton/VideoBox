package go.videobox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import go.videobox.Mathem;

import java.util.ArrayList;
import java.util.Locale;

import go.videobox.R;



public class PlaylistActivityListViewAdapter extends BaseAdapter {

    private ArrayList<PlaylistItem> data = new ArrayList<>();
    private Context context;

    public PlaylistActivityListViewAdapter(Context context, ArrayList<PlaylistItem> arr) {
        if (arr != null) {
            data = arr;
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int num) {
        // TODO Auto-generated method stub
        return data.get(num);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int i, View someView, ViewGroup arg2) {

        LayoutInflater inflater = LayoutInflater.from(context);
        if (someView == null) {
            someView = inflater.inflate(R.layout.list_view_item, arg2, false);
        }
        // ImageView imageView = (ImageView) someView.findViewById(R.id.img);
        TextView header = (TextView) someView.findViewById(R.id.item_headerText);
        TextView subHeader = (TextView) someView.findViewById(R.id.item_subHeaderText);
        TextView positiontext = (TextView) someView.findViewById(R.id.tv_progress_position);
        TextView progresstext = (TextView) someView.findViewById(R.id.tv_progress);
        ProgressBar pb = (ProgressBar) someView.findViewById(R.id.progress);

        header.setTextColor(context.getResources().getColor(R.color.header_font_color));
        header.setText(data.get(i).mSubHeader);
        subHeader.setTextColor(context.getResources().getColor(R.color.sub_header_font_color));
        subHeader.setText(data.get(i).mSubsubHeader);


        pb.setProgress(Mathem.positionfilm(data.get(i).mPosition,data.get(i).mDuration));
        if (data.get(i).mDuration==0){
            progresstext.setVisibility(View.INVISIBLE);
            positiontext.setVisibility(View.INVISIBLE);
       } else {
            progresstext.setVisibility(View.VISIBLE);
            positiontext.setVisibility(View.VISIBLE);

            progresstext.setText(Integer.toString(Mathem.positionfilm(data.get(i).mPosition,data.get(i).mDuration))+"%");
            positiontext.setText(Mathem.timetohours(data.get(i).mPosition)+"  из  " +Mathem.timetohours(data.get(i).mDuration));
        }


        return someView;
    }

}

