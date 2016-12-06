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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import go.videobox.adapters.RecentActivityGridViewAdapter;
import go.videobox.adapters.RecentGridItem;
import go.videobox.dbClass.FilmData;
import go.videobox.dbClass.FilmHeader;

public class RecentActivity extends AppCompatActivity {
     ArrayList<RecentGridItem> arrayList1 = new ArrayList<>();
    GridView gv;
    List<FilmData> FilmlistData;





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


        RecentGridItem tempitem = new RecentGridItem("","",false,"","",0,0,new Date());//создание массива плиток фильмов
    for (FilmHeader film:queryResults) {


        tempitem = new RecentGridItem("","",false,"","",0,0,new Date());
        tempitem.mHeader=film.mHeader;
        tempitem.mPosterUrl=film.mPosterUrl;
        tempitem.mSerialFlag=film.mSerialFlag;
        tempitem.mUrl=film.mUrl;
        tempitem.mDateWatch=film.mDateWatch;

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
        startActivityForResult(playerIntent,MainActivity.REQUEST_CODE);

    }

    private void clickrecentgrid(Integer position){
        RecentGridItem item;
        item=arrayList1.get(position);
       if (!item.mSerialFlag){
          startplayer(item.mUrl);
       }
        else {
           Log.d("ololo", "плейлист азазаз");
        //старт распознавания плейлиста и старт плейлист активити
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



}
