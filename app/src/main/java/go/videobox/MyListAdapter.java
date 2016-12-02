package go.videobox;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    private ArrayList<PlaylistItem> data = new ArrayList<>();
    private Context context;

    public MyListAdapter(Context context, ArrayList<PlaylistItem> arr) {
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
        header.setTextColor(context.getResources().getColor(R.color.header_font_color));
        header.setText(data.get(i).mSubHeader);
        subHeader.setTextColor(context.getResources().getColor(R.color.sub_header_font_color));
        subHeader.setText(data.get(i).mSubsubHeader);
        ProgressBar pb = (ProgressBar) someView.findViewById(R.id.progress);
        pb.setProgress(positionfilm(data.get(i).mPosition,data.get(i).mDuration));
        TextView progresstext = (TextView) someView.findViewById(R.id.tv_progress);
        progresstext.setText(Integer.toString(positionfilm(data.get(i).mPosition,data.get(i).mDuration))+"%");
        return someView;
    }
    private static Integer positionfilm (Integer pos, Integer dur){
        if ((pos==0)&&(dur==0))return 0;
        return  (int)(((double)pos/(double)dur) * 100);
    }
}