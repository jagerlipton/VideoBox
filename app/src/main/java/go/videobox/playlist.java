package go.videobox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

public class playlist extends AppCompatActivity {
    private ArrayList<PlaylistItem> playList = new ArrayList<>();
    String poster_url="";
    String poster_header="";
    String poster_sub_header="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        poster_url=intent.getStringExtra("poster_url");
        poster_header=intent.getStringExtra("poster_header");
        poster_sub_header=intent.getStringExtra("poster_subheader");

        if (bundle != null) {
            playList = (ArrayList<PlaylistItem>) bundle.getSerializable("series");
            ListView list = (ListView) findViewById(R.id.playlistview);
            list.setAdapter(new MyListAdapter(this, playList));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0) startplayer(playList.get(position).url);
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ImageView imageView = (ImageView) findViewById(R.id.img);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        imageLoader.displayImage(poster_url, imageView, options);

         TextView header_text  = (TextView) findViewById(R.id.item_headerText);
         TextView subheader_text  = (TextView) findViewById(R.id.item_subHeaderText);
        header_text.setText(poster_header);
        subheader_text.setText(poster_sub_header);
    }

    private void startplayer(String flvurl){
        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
        startActivity(playerIntent);
    }

}
