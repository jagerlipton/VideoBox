package go.videobox;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    String url = "http://txt.newsru.com/";
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter arrayAdapter;
    GridView gv;
    Context context;
    ArrayList prgmName;
    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
    public static int [] prgmImages={R.drawable.images,R.drawable.images1,R.drawable.images2,R.drawable.images3,R.drawable.images4,R.drawable.images5,R.drawable.images6,R.drawable.images7,R.drawable.images8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayList=new ArrayList<>();
        setContentView(R.layout.activity_main);

        ///listView=(ListView)findViewById(R.id.listView1);
     //   arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
       // listView.setAdapter(arrayAdapter);
        gv=(GridView) findViewById(R.id.gridView1);
        gv.setAdapter(new CustomAdapter(this, prgmNameList,prgmImages));

        new MyTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }




    public class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document document = Jsoup.connect(url).get();
                Elements description = document.select(".headcolumn");

                for(Element titleFromSite:description){
                    if(titleFromSite.text().equals(""))
                        continue;
                    arrayList.add(titleFromSite.text());
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

          //  arrayAdapter.notifyDataSetChanged();
        }
    }



}
