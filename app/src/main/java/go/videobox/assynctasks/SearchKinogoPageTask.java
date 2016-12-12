package go.videobox.assynctasks;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import go.videobox.adapters.Item;
import go.videobox.base.AbstractTaskLoader;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.base.TaskProgressDialogFragment;

public class SearchKinogoPageTask extends AbstractTaskLoader {

    private Bundle mBundle;
    private final static String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private ArrayList<Item> arrayList = new ArrayList<>();
    private final static String searchpagelink="http://kinogo.club/index.php?do=search&subaction=search&search_start=0&full_search=0&result_from=1&titleonly=3&story=";


    public static void execute(FragmentActivity fa, ITaskLoaderListener taskLoaderListener, Bundle arguments) {

        SearchKinogoPageTask loader = new SearchKinogoPageTask(fa,arguments);

        new TaskProgressDialogFragment.Builder(fa, loader,"Wait...")
                .setCancelable(true)
                .setTaskLoaderListener(taskLoaderListener)
                .show();
    }

    protected SearchKinogoPageTask(Context context, Bundle arguments) {
        super(context);
        if (arguments != null)  this.mBundle=arguments;
    }

    @Override
    public Object loadInBackground() {
        String urlpage = mBundle.getString("urlpage");

        try {
            arrayList.clear();
            String normalUrl= URLEncoder.encode(urlpage, "Windows-1251");
            Document document = Jsoup.connect(searchpagelink+normalUrl).userAgent(userAgent).get();
            Elements shortstory = document.select(".shortstory");
            Elements headers = document.select(".zagolovki");
            Elements imgs = shortstory.select(".shortimg");

            for(Element titleFromSite:headers){// парсинг заголовков и ссылок на страницу с фильмом
                String filmName = titleFromSite.text();
                Element link= titleFromSite.select("a").first();
                String linkHref = link.attr("href");

                Item tempitem = new Item("","","",0,0,"","");//создание массива плиток фильмов
                tempitem.mHeader=filmName;
                tempitem.mPagelink=linkHref;
                arrayList.add(tempitem);
            }

            Integer i=0;
            for(Element img:imgs){   //парсинг постеров и описания
                if (imgs.size()==headers.size()){
                    String subtext=img.text();
                    Element link= img.select("img").first();
                    String linkHref = link.absUrl("src");
                    System.out.println(linkHref);
                    arrayList.get(i).mSubHeader=subtext;
                    arrayList.get(i).mPictureurl=linkHref;
                    i++;
                }
            }

            if (isCanselled()) {
                //      break;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        Bundle mbundle = new Bundle();
        mbundle.putSerializable("SearchKinogoPageTask",arrayList);
        mbundle.putString("header","SearchKinogoPageTask");
        return mbundle;
    }
    @Override
    public Bundle getArguments() {
        return null;
    }

    @Override
    public void setArguments(Bundle args) {
    }
}

