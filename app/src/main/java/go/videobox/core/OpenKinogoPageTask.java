package go.videobox.core;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import go.videobox.CustomAdapter;
import go.videobox.GlobalData;
import go.videobox.Item;
import go.videobox.MainActivity;
import go.videobox.R;


public class OpenKinogoPageTask extends AsyncTask<String, Void, ArrayList<Item>>  {


   private final static String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
   private  ArrayList<Item> arrayList = new ArrayList<>();
    GridView gv;
    private Context context;
    public OpenKinogoPageTask(Context context) { // Это конструктор, куда передаю context
        this.context = context;
    }

    @Override
    protected  ArrayList<Item>  doInBackground(String...parameter) {
        String urlpage="";
        for (String p : parameter)urlpage=p;

        try {
            arrayList.clear();
            Document document = Jsoup.connect(urlpage).userAgent(userAgent).get();
            Elements shortstory = document.select(".shortstory");
            Elements headers = shortstory.select(".zagolovki");
            Elements imgs = shortstory.select(".shortimg");

            for(Element titleFromSite:headers){ //получение названия фильма
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
            for(Element img:imgs){   // получение постера и описания
                if (imgs.size()==headers.size()){
                    String subtext=img.text();
                    Element link= img.select("a").first();
                    String linkHref = link.attr("href");
                    arrayList.get(i).subheader=subtext;
                    arrayList.get(i).pictureurl=linkHref;
                    i++;
                }
            }
            Elements buttons = document.select(".bot-navigation"); // получение ссылок на кнопки назад-вперед
            for(Element button:buttons){
                Element link1= button.select("a").first();
                GlobalData.Gd_back_pagelink = link1.attr("href");
                Element link2= button.select("a").last();
                GlobalData.Gd_forward_pagelink = link2.attr("href");
            }

            Element span= buttons.select("span").first();
            String temp = span.text();
          //  if (temp.equalsIgnoreCase("Раньше")) switchBackButton(false);
         //   else switchBackButton(true);

            Element span2= buttons.select("span").last();
            String temp2 = span2.text();
         //   if (temp2.equalsIgnoreCase("Позже")) switchForwardButton(false);
         //   else switchForwardButton(true);

        }catch (IOException ex){
            ex.printStackTrace();
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Item> list) {
           super.onPostExecute(list);
        CustomAdapter adapter = new CustomAdapter (context, arrayList);
        GridView gv = (GridView) ((Activity) context).findViewById(R.id.gridView1);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(MainActivity.arrayList.size()>0) MainActivity.arrayList.clear();
        MainActivity.arrayList.addAll(arrayList);

    }
}
