package go.videobox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class RecentActivity extends AppCompatActivity {
    private ArrayList<Item> arrayList = new ArrayList<>();
    GridView gv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        gv=(GridView) findViewById(R.id.gridViewRecent);


        Item tempitem = new Item("","","","","","","","");//создание массива плиток фильмов
       for (int i=0; i<=30; i++) {

        tempitem.header="olololo";
        tempitem.pictureurl="http://kinogo.club/uploads/posts/2016-11/thumbs/1480048379_pokazhenschinyspyat.jpg";
        tempitem.pagelink="";
        arrayList.add(tempitem);}

        CustomAdapter adapter = new CustomAdapter (RecentActivity.this, arrayList);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
