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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import go.videobox.adapters.PlaylistActivityListViewAdapter;
import go.videobox.adapters.PlaylistItem;
import go.videobox.dbClass.FilmHeader;

public class PlaylistActivity extends AppCompatActivity {
    private ArrayList<PlaylistItem> playList = new ArrayList<>();
    String poster_url="";
    String poster_header="";
    String poster_sub_header="";

    static  String mmmHeader="";
    static String mmmSubHeader="";


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
            list.setAdapter(new PlaylistActivityListViewAdapter(this, playList));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0) startplayer(position);
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

         TextView header_text  = (TextView) findViewById(R.id.item_headerText);  // название сериала
         TextView subheader_text  = (TextView) findViewById(R.id.item_subHeaderText); // описание сериала
        header_text.setText(poster_header);
        subheader_text.setText(poster_sub_header);
    }

    private void startplayer(Integer pos){
        String flvurl = playList.get(pos).mUrl;
        MainActivity.checkWatchSerialFilm(poster_header,poster_url,flvurl,poster_sub_header);
        mmmHeader=playList.get(pos).mHeader;
        mmmSubHeader=playList.get(pos).mSubHeader;
        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
        playerIntent.putExtra(MainActivity.EXTRA_RETURN_RESULT, true);
        startActivityForResult(playerIntent,MainActivity.REQUEST_CODE);


    }

    private static void appendDetails( StringBuilder sb, Object object )
    {
        if( object != null && object.getClass().isArray() )
        {
            sb.append('[');

            int length = Array.getLength(object);
            for( int i = 0; i < length; ++i )
            {
                if( i > 0 )
                    sb.append(", ");

                appendDetails(sb, Array.get(object, i));
            }

            sb.append(']');
        }
        else if( object instanceof Collection)
        {
            sb.append('[');

            boolean first = true;
            for( Object element : (Collection)object )
            {
                if( first )
                    first = false;
                else
                    sb.append(", ");

                appendDetails(sb, element);
            }

            sb.append(']');
        }
        else
            sb.append(object);
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( requestCode == MainActivity.REQUEST_CODE )
        {
            switch( resultCode )
            {
                case RESULT_OK:
                    //    Log.i( "ololo", "Ok: " + data );
                    break;

                case RESULT_CANCELED:
                    //     Log.i( "ololo", "Canceled: " + data );
                    break;

                case MainActivity.RESULT_ERROR:
                    //    Log.e( "ololo", "Error occurred: " + data );
                    break;

                default:
                    //   Log.w( "ololo", "Undefined result code (" + resultCode  + "): " + data );
                    break;
            }

            if( data != null )
                dumpParams(data);

			/*
			 * (YOUR CODE HERE) Handle result.
			 */

            finish();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private static void dumpParams( Intent intent )
    {
        StringBuilder sb = new StringBuilder();
        Bundle extras = intent.getExtras();

        sb.setLength(0);
        sb.append("* dat=").append(intent.getData());
        //  Log.v("ololo", sb.toString());

        sb.setLength(0);
        sb.append("* typ=").append(intent.getType());
        //  Log.v("ololo", sb.toString());

        if( extras != null && extras.size() > 0 )
        {
            sb.setLength(0);

            Integer p=1;
            Integer d=1;
            int i = 0;
            for( String key : extras.keySet() )
            {
                if (key.equals("duration")){

                    sb.setLength(0);
                    appendDetails( sb, extras.get( key ) );
                    String duration =sb.toString();
                    d= Integer.parseInt(duration);
                    // / Log.v("ololo1", d.toString());

                }
                if (key.equals("position")){
                    sb.setLength(0);
                    appendDetails( sb, extras.get( key ) );
                    String position =sb.toString();
                    p= Integer.parseInt(position);
                    //  Log.v("ololo1", p.toString());
                }

            }
            FilmHeader mfilm = new FilmHeader();
            mfilm.updatePositionFilm(mmmHeader,mmmSubHeader,p,d);
          //  positionfilm(p,d);
            // Log.v("ololo1",positionfilm(p,d).toString());

        }
    }

    public static Integer positionfilm (Integer pos, Integer dur){
        return  (int)(((double)pos/(double)dur) * 100);
    }

}
