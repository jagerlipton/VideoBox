package go.videobox;

import go.videobox.Mathem;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.fastadapter.utils.RecyclerViewCacheUtil;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String pagelink = "";
    String searchpagelink="http://kinogo.club/index.php?do=search&subaction=search&search_start=0&full_search=0&result_from=1&titleonly=3&story=";
    String back_pagelink="";
    String forward_pagelink="";
    String searchstring="";
    String poster_url="";
    String poster_header="";
    String poster_sub_header="";
    String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private AccountHeader headerResult = null;
    private Drawer result = null;
    ExpandableDrawerItem category = new  ExpandableDrawerItem();
    ExpandableDrawerItem year = new  ExpandableDrawerItem();
    ExpandableDrawerItem serials = new  ExpandableDrawerItem();



    private  ArrayList<Item> arrayList = new ArrayList<>();
    private  ArrayList<PlaylistItem> playList = new ArrayList<>();
    ListView listView;
    GridView gv;
    Context context;
    ArrayList prgmName;



    ///Log.d("ololo",  Integer.toString(i));
    //  Toast.makeText(MainActivity.this, Integer.toString(i), Toast.LENGTH_SHORT).show();
    //System.out.println(st);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchstring=query;
                opensearchpage(searchpagelink);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
/// create resume============================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv=(GridView) findViewById(R.id.gridView1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        category = new ExpandableDrawerItem().withName(R.string.drawer_item_category).withIdentifier(2).withSelectable(false);
        year = new ExpandableDrawerItem().withName(R.string.drawer_item_year).withIdentifier(3).withSelectable(false);
        serials = new ExpandableDrawerItem().withName(R.string.drawer_item_serials).withIdentifier(4).withSelectable(false);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withSelectedItem(-1)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new  PrimaryDrawerItem().withName(R.string.drawer_item_startpage).withIdentifier(7).withSelectable(false),
                              new DividerDrawerItem(),
                        new  PrimaryDrawerItem().withName(R.string.drawer_item_history).withIdentifier(6).withSelectable(false),
                        new DividerDrawerItem(),
                        category.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fighting).withLevel(2).withIdentifier(201).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_detective).withLevel(2).withIdentifier(202).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_documental).withLevel(2).withIdentifier(203).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_drama).withLevel(2).withIdentifier(204).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_historic).withLevel(2).withIdentifier(205).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_comedy).withLevel(2).withIdentifier(206).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_criminal).withLevel(2).withIdentifier(207).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_melodrama).withLevel(2).withIdentifier(208).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_mult).withLevel(2).withIdentifier(209).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_musicle).withLevel(2).withIdentifier(210).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_ussr).withLevel(2).withIdentifier(211).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_adventures).withLevel(2).withIdentifier(212).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_family).withLevel(2).withIdentifier(213).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_sport).withLevel(2).withIdentifier(214).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_triller).withLevel(2).withIdentifier(215).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_horror).withLevel(2).withIdentifier(216).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fantasy).withLevel(2).withIdentifier(217).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_fantastika).withLevel(2).withIdentifier(218).withSelectable(false)),
                        year.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2016).withLevel(2).withIdentifier(301).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2015).withLevel(2).withIdentifier(302).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_year2014).withLevel(2).withIdentifier(303).withSelectable(false)),
                        serials.withSubItems(
                                new PrimaryDrawerItem().withName(R.string.drawer_item_serial_russian).withLevel(2).withIdentifier(401).withSelectable(false),
                                new PrimaryDrawerItem().withName(R.string.drawer_item_serial_notrussian).withLevel(2).withIdentifier(402).withSelectable(false)),

                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exit).withIdentifier(5).withSelectable(false)



                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.getIdentifier() == 7) {
                          openpage(getResources().getString(R.string.link_item_startpage));
                        } else if (drawerItem.getIdentifier() == 1) {
                          //search


                        } else if (drawerItem.getIdentifier() == 5) {
                            MainActivity.this.finish();//exit
                        } else if (drawerItem.getIdentifier() == 6) {
                         //history
                        } else if (drawerItem.getIdentifier() == 201) {
                            openpage(getResources().getString(R.string.link_item_fighting));
                        } else if (drawerItem.getIdentifier() == 202) {
                            openpage(getResources().getString(R.string.link_item_detective));
                        } else if (drawerItem.getIdentifier() == 203) {
                            openpage(getResources().getString(R.string.link_item_documental));
                        } else if (drawerItem.getIdentifier() == 204) {
                            openpage(getResources().getString(R.string.link_item_drama));
                        } else if (drawerItem.getIdentifier() == 205) {
                            openpage(getResources().getString(R.string.link_item_historic));
                        } else if (drawerItem.getIdentifier() == 206) {
                            openpage(getResources().getString(R.string.link_item_comedy));
                        } else if (drawerItem.getIdentifier() == 207) {
                            openpage(getResources().getString(R.string.link_item_criminal));
                        } else if (drawerItem.getIdentifier() == 208) {
                            openpage(getResources().getString(R.string.link_item_melodrama));
                        } else if (drawerItem.getIdentifier() == 209) {
                            openpage(getResources().getString(R.string.link_item_mult));
                        } else if (drawerItem.getIdentifier() == 210) {
                            openpage(getResources().getString(R.string.link_item_musicle));
                        } else if (drawerItem.getIdentifier() == 211) {
                            openpage(getResources().getString(R.string.link_item_ussr));
                        } else if (drawerItem.getIdentifier() == 212) {
                            openpage(getResources().getString(R.string.link_item_adventures));
                        } else if (drawerItem.getIdentifier() == 213) {
                            openpage(getResources().getString(R.string.link_item_family));
                        } else if (drawerItem.getIdentifier() == 214) {
                            openpage(getResources().getString(R.string.link_item_sport));
                        } else if (drawerItem.getIdentifier() == 215) {
                            openpage(getResources().getString(R.string.link_item_triller));
                        } else if (drawerItem.getIdentifier() == 216) {
                            openpage(getResources().getString(R.string.link_item_horror));
                        } else if (drawerItem.getIdentifier() == 217) {
                            openpage(getResources().getString(R.string.link_item_fantasy));
                        } else if (drawerItem.getIdentifier() == 218) {
                            openpage(getResources().getString(R.string.link_item_fantastika));
                        } else if (drawerItem.getIdentifier() == 301) {
                            openpage(getResources().getString(R.string.link_item_year2016));
                        } else if (drawerItem.getIdentifier() == 302) {
                            openpage(getResources().getString(R.string.link_item_year2015));
                        } else if (drawerItem.getIdentifier() == 303) {
                            openpage(getResources().getString(R.string.link_item_year2014));
                        } else if (drawerItem.getIdentifier() == 401) {
                            openpage(getResources().getString(R.string.link_item_serial_russian));
                        } else if (drawerItem.getIdentifier() == 402) {
                            openpage(getResources().getString(R.string.link_item_serial_notrussian));
                        }
                        return false;
                    }
                })
                .build();


        new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());

        openpage(getResources().getString(R.string.link_item_startpage));


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) clickgrid(position);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //=======================================================================================


    public void clickgrid(Integer index){
        Item item;
        item=arrayList.get(index);
        getflv(item.pagelink);
        poster_url=item.pictureurl;
        poster_header=item.header;
        poster_sub_header=item.subheader;
      //  Log.d("ololo", );
    }

    private void opensearchpage(String link){
        pagelink=link;
        new SearchTask().execute();

    }
    private void openpage(String link){
        pagelink=link;
        new MyTask().execute();

    }

    private void getflv(String httpurl){
        new GetPlayerTask().execute(httpurl);
    }


    private void startplayer(String flvurl){
        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flvurl));
        startActivity(playerIntent);
    }



    private void viewSeriesList(){
        Intent playerIntent = new Intent(MainActivity.this, playlist.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("series", playList);
        playerIntent.putExtras(bundle);
        playerIntent.putExtra("poster_url",poster_url);
        playerIntent.putExtra("poster_header",poster_header);
        playerIntent.putExtra("poster_subheader",poster_sub_header);

        startActivity(playerIntent);

    }

    public class GetPlayerTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... parameter) {
            String flvurl="";
            for (String p : parameter)flvurl=p;

            try {

                Document document = Jsoup.connect(flvurl).userAgent(userAgent).get();
                Elements encodeplayer = document.select(".box.visible");
                Elements scriptElements = encodeplayer.tagName("script");
                String encodedstring="";
                String decodedstring="";
                for(Element encodedplayer:scriptElements)encodedstring=encodedplayer.data();
                encodedstring=encodedstring.substring(encodedstring.indexOf("'")+1,encodedstring.length());
                encodedstring=encodedstring.substring(0,encodedstring.indexOf("'"));

                encodedstring=Mathem.base64_decode(encodedstring);
                Document decodedplayerDoc = Jsoup.parse(encodedstring);
                Elements decodedplayerElements = decodedplayerDoc.select(".uppod_style_video");
                for(Element titleFromSite:decodedplayerElements) {
                    Element el = titleFromSite.select("param").last();
                    decodedstring = el.val();

                }

                //String st=decodedstring.substring(decodedstring.indexOf("st=")+3,decodedstring.indexOf("&file"));

                if (decodedstring.indexOf("file=")>0){
                String file=decodedstring.substring(decodedstring.indexOf("file=")+5,decodedstring.indexOf("&poster"));
                flvurl=Mathem.deup(file,Mathem.hash1);

                } else if (decodedstring.indexOf("pl=")>0) {
                    flvurl=decodedstring.substring(decodedstring.indexOf("pl=")+3,decodedstring.indexOf("&poster"));

                }

                   }catch (IOException ex){
                ex.printStackTrace();
            }

            return flvurl;


        }

        protected void onPostExecute(String flvurl) {
            super.onPostExecute(flvurl);
          if (flvurl.indexOf(".flv")>0) startplayer(flvurl);
                  else if (flvurl.indexOf(".txt")>0)  new getPlaylist().execute(flvurl);

             }
    }

  //===========

    private PlaylistItem parsePlaylistLine(String s){
        PlaylistItem pItem= new PlaylistItem("","","","");

        if (s.indexOf("comment")>0)pItem.header=s.substring(s.indexOf(":")+2,s.indexOf("<br>"));
        if (s.indexOf("<br>")>0)pItem.subheader=s.substring(s.indexOf("<br>")+4,s.indexOf("\",\"file\":\""));
        if (s.indexOf("\",\"file\":\"")>0) pItem.url=s.substring(s.indexOf("\",\"file\":\"")+10,s.indexOf("\"}"));

        return pItem;
    }

    private class  getPlaylist extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... parameter) {
            String result = "";
            String listurl = "";
            for (String p : parameter) listurl = p;

            try {
                playList.clear();
                URL url = new URL(listurl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;

                PlaylistItem pItem = new PlaylistItem("","","","");
                while ((str = in.readLine()) != null) {
                    result += str;
                    if ((!str.isEmpty())&&(str.indexOf("comment")>0)) playList.add(parsePlaylistLine(str));

                }
                in.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }




  @Override
        protected void onPostExecute(Void aVoid)
        {
            //System.out.println("startttscuk");
            viewSeriesList();
           //
        }
    }



    public class SearchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                arrayList.clear();

               String normalUrl= URLEncoder.encode(searchstring, "Windows-1251");
               Document document = Jsoup.connect(searchpagelink+normalUrl).userAgent(userAgent).get();
               Elements shortstory = document.select(".shortstory");
               Elements headers = document.select(".zagolovki");
               Elements imgs = shortstory.select(".shortimg");

                for(Element titleFromSite:headers){// парсинг заголовков и ссылок на страницу с фильмом
                    String filmName = titleFromSite.text();
                    Element link= titleFromSite.select("a").first();
                    String linkHref = link.attr("href");

                    Item tempitem = new Item("","","","","","","","");//создание массива плиток фильмов
                    tempitem.header=filmName;
                    tempitem.pagelink=linkHref;
                    arrayList.add(tempitem);
                }

                Integer i=0;
                for(Element img:imgs){   //парсинг постеров и описания
                    if (imgs.size()==headers.size()){
                        String subtext=img.text();
                        Element link= img.select("img").first();
                        String linkHref = link.absUrl("src");
                        System.out.println(linkHref);
                        arrayList.get(i).subheader=subtext;
                        arrayList.get(i).pictureurl=linkHref;
                        i++;
                    }
                }

            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            CustomAdapter adapter = new CustomAdapter (MainActivity.this, arrayList);
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    public class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                arrayList.clear();
                Document document = Jsoup.connect(pagelink).userAgent(userAgent).get();
                Elements shortstory = document.select(".shortstory");
                Elements headers = shortstory.select(".zagolovki");
                Elements imgs = shortstory.select(".shortimg");

                    for(Element titleFromSite:headers){
                    if(titleFromSite.text().equals(""))   continue;
                        String filmName = titleFromSite.text();
                        Element link= titleFromSite.select("a").first();
                        String linkHref = link.attr("href");


                    Item tempitem = new Item("","","","","","","","");
                    tempitem.header=filmName;
                    tempitem.subheader=" ";
                    tempitem.pagelink=linkHref;
                     arrayList.add(tempitem);
                }
                Integer i=0;
                for(Element img:imgs){
                    if (imgs.size()==headers.size()){
                        String subtext=img.text();
                        Element link= img.select("a").first();
                        String linkHref = link.attr("href");
                        arrayList.get(i).subheader=subtext;
                        arrayList.get(i).pictureurl=linkHref;
                    i++;
                    }
                }
                Elements buttons = document.select(".bot-navigation");
                for(Element button:buttons){
                    Element link1= button.select("a").first();
                    back_pagelink = link1.attr("href");
                    Element link2= button.select("a").last();
                    forward_pagelink = link2.attr("href");
                }

                Element span= buttons.select("span").first();
                String temp = span.text();
                if (temp.equalsIgnoreCase("Раньше")) switchBackButton(false);
                   else switchBackButton(true);

                Element span2= buttons.select("span").last();
                String temp2 = span2.text();
                if (temp2.equalsIgnoreCase("Позже")) switchForwardButton(false);
                else switchForwardButton(true);

                }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            CustomAdapter adapter = new CustomAdapter (MainActivity.this, arrayList);
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
          if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }

    }
    public void back_click (View v) {

        openpage(back_pagelink);
    }
    public void forward_click (View v) {

       openpage(forward_pagelink);
    }

    private void switchBackButton(Boolean flag){

        Button backbutton=(Button) findViewById(R.id.backward);
          backbutton.setClickable(flag);

    }
    private void switchForwardButton(Boolean flag){

        Button forwardbutton=(Button) findViewById(R.id.forward);
        forwardbutton.setClickable(flag);
    }
}
