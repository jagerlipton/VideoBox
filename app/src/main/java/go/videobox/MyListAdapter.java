package go.videobox;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
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
        header.setText(data.get(i).header);
        subHeader.setTextColor(context.getResources().getColor(R.color.sub_header_font_color));
        subHeader.setText(data.get(i).subheader);


        return someView;
    }

}