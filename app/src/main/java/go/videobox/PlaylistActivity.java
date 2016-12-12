package go.videobox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import go.videobox.dbClass.WorkWithDB;

public class PlaylistActivity extends AppCompatActivity {
    private ArrayList<PlaylistItem> playList = new ArrayList<>();
    String poster_url="";  // линк тхт на сериал
    String poster_header="";  //  имя сериала
    String poster_sub_header="";   // описание сериала
  //  String poster_txturl="";

    static  String mmmHeader="";
    static String mmmSubHeader="";
    private static final int REQUEST_CODE = 0x8002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        poster_url=intent.getStringExtra("poster_url");
        poster_header=intent.getStringExtra("poster_header");
        poster_sub_header=intent.getStringExtra("poster_subheader");
      //  poster_txturl=intent.getStringExtra("poster_url_serial");


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

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }

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

   //-----------------------------------------------------------------

    //---------------------------------------------------------------


    private void startplayer(Integer pos){
        String flvurl = playList.get(pos).mUrlSeries; //мурл на flv серию
        String txturl = playList.get(pos).mUrlSerial; //мурл на txt
        mmmHeader=playList.get(pos).mHeader; //имя сериала
        mmmSubHeader=playList.get(pos).mSubHeader; // номер серии
        WorkWithDB.checkWatchSerialFilm(poster_header,poster_url,txturl,flvurl,mmmSubHeader,poster_sub_header); //проверка тоже должны быть уже с flv серией

      //  Log.d("ololo",poster_header); // имя сериала
      //  Log.d("ololo",poster_sub_header);//  описание.
        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
        playerIntent.putExtra(MainActivity.EXTRA_RETURN_RESULT, true);
        if (playList.get(pos).mPosition>0) {
            MXPlayerResults.MXOptions options = new MXPlayerResults.MXOptions();
            options.resumeAt = playList.get(pos).mPosition;
            if( options != null )   options.putToIntent(playerIntent);
        }

        startActivityForResult(playerIntent,REQUEST_CODE);


    }



    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        if( requestCode ==REQUEST_CODE )
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
                MXPlayerResults.dumpParams(data,mmmHeader,mmmSubHeader);
            finish();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }






}
