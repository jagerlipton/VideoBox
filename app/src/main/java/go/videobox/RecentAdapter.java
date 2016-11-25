package go.videobox;

//Custom grid adapter with image (get url)


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

class RecentAdapter extends BaseAdapter {

    Context context;
    ArrayList<Item> items;
    private static LayoutInflater inflater=null;
    RecentAdapter(Context context, ArrayList<Item> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.griditem, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView header = (TextView) convertView.findViewById(R.id.header);
       // TextView subheader = (TextView) convertView.findViewById(R.id.subheader);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        imageLoader.displayImage(items.get(position).pictureurl, imageView, options);
        header.setText(items.get(position).header);
     //   subheader.setText(items.get(position).subheader);
        return convertView;
    }

}