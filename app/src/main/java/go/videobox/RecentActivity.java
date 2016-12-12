package go.videobox;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import go.videobox.adapters.Item;
import go.videobox.adapters.MainPageGridViewAdapter;
import go.videobox.adapters.PlaylistItem;
import go.videobox.adapters.RecentActivityGridViewAdapter;
import go.videobox.adapters.RecentGridItem;
import go.videobox.assynctasks.GetPlaylistKinogo;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.dbClass.FilmData;
import go.videobox.dbClass.FilmHeader;

public class RecentActivity extends AppCompatActivity implements ITaskLoaderListener {
     ArrayList<RecentGridItem> arrayList1 = new ArrayList<>();
    GridView gv;
    List<FilmData> FilmlistData;
    private ArrayList<PlaylistItem> playList = new ArrayList<>();
    static String poster_url=""; //  ссылка на постер картинку
    static  String poster_header="";// название фильма
    static  String poster_sub_header="";// описание фильма
    private static final int REQUEST_CODE = 0x8003;



    @Override
    public void onCancelLoad() {
        Log.d("ololo", "task canceled");
    }

    private void setМainPageAdapter (){
    //    MainPageGridViewAdapter adapter = new MainPageGridViewAdapter (this, arrayList);
    //    gv=(GridView) findViewById(R.id.gridView1);
     //   gv.setAdapter(adapter);
     //   adapter.notifyDataSetChanged();
    }

    @Override  // сюда возвращает результат из таска любой.
    public void onLoadFinished(Object data) {
        if(data!=null && data instanceof Bundle){

            if (((Bundle) data).getString("header").equals("GetPlaylistKinogo")) {
                playList = (ArrayList<PlaylistItem>) ((Bundle) data).getSerializable("GetPlaylistKinogo");
                viewSeriesList(); // передаем в интент данные и открываем активити плейлиста
            }



//

        }
    }
    private void viewSeriesList(){
        Intent playerIntent = new Intent(RecentActivity.this, PlaylistActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("series", playList);
        playerIntent.putExtras(bundle);
        playerIntent.putExtra("poster_url",poster_url);
        playerIntent.putExtra("poster_header",poster_header);
        playerIntent.putExtra("poster_subheader",poster_sub_header);

        startActivity(playerIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        gv=(GridView) findViewById(R.id.gridViewRecent);
        registerForContextMenu(gv);


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) clickrecentgrid(position);
            }

        });


        FilmHeader sHead = new FilmHeader();
        List<FilmHeader> queryResults =sHead.getFifty();


        RecentGridItem tempitem = new RecentGridItem("","",false,"","",0,0,new Date(),"","");//создание массива плиток фильмов
    for (FilmHeader film:queryResults) {


        tempitem = new RecentGridItem("","",false,"","",0,0,new Date(),"","");
        tempitem.mHeader=film.mHeader;
        tempitem.mDescription=film.mDescription;

        List<FilmData> slist= film.getFilmList();
       if (slist.size()>0) tempitem.mSubHeader= slist.get(0).mSubHeader;



        tempitem.mPosterUrl=film.mPosterUrl;
        tempitem.mSerialFlag=film.mSerialFlag;
        tempitem.mUrl=film.mUrl;
        tempitem.mDateWatch=film.mDateWatch;
        tempitem.mProfile=film.mProfile;

            arrayList1.add(tempitem);

       }





        for (RecentGridItem itemmm :arrayList1)    System.out.println(itemmm.mDateWatch);
        RecentActivityGridViewAdapter adapter = new RecentActivityGridViewAdapter (RecentActivity.this, arrayList1);


       gv.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
  //      Intent intent = getIntent();
     ///   String headshot = intent.getStringExtra("headshot");
//        Log.d("ololo", headshot);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recent_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_item:
                delete_recent_item(info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    private void startplayer(String flvurl){

        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
        playerIntent.putExtra(MainActivity.EXTRA_RETURN_RESULT, true);
        startActivityForResult(playerIntent,REQUEST_CODE);

    }

    private void clickrecentgrid(Integer position){
        RecentGridItem item;
        item=arrayList1.get(position);
       if (!item.mSerialFlag){
          startplayer(item.mUrl);
       }
        else {
       //    Log.d("ololo", "плейлист азазаз");
        //старт распознавания плейлиста и старт плейлист активити
           Bundle myb = new Bundle();
           myb.putString("urlpage",item.mUrl);
           myb.putString("poster_header",item.mHeader);
           myb.putString("poster_url",item.mPosterUrl);
           poster_header=item.mHeader;
           poster_sub_header=item.mDescription;
           poster_url=item.mPosterUrl;
//хедер есть, сабхедера нет, постер есть.
           //в мурл попала ссылка на файл серию
         //  System.out.println(item.mUrl); System.out.println(item.mHeader);System.out.println(item.mPosterUrl);
           Log.d("ololo", item.mUrl);
           GetPlaylistKinogo.execute(this, this,myb);
       }

    }
    private void delete_recent_item(Integer position){

        RecentActivityGridViewAdapter adapter = new RecentActivityGridViewAdapter (RecentActivity.this, arrayList1);
        gv.setAdapter(adapter);


        FilmHeader sHead = new FilmHeader();
        new Delete().from(FilmHeader.class).where("Header = ?", arrayList1.get(position).mHeader).execute();

        adapter.items.remove(adapter.getItem(position));
        adapter.notifyDataSetChanged();
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
                MXPlayerResults.dumpParams(data,poster_header,poster_sub_header);

		           finish();
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }



}
