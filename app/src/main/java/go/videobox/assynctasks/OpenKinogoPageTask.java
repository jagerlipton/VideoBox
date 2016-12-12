package go.videobox.assynctasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import go.videobox.GlobalData;
import go.videobox.adapters.Item;
import go.videobox.base.AbstractTaskLoader;
import go.videobox.base.ITaskLoaderListener;
import go.videobox.base.TaskProgressDialogFragment;

public class OpenKinogoPageTask extends AbstractTaskLoader  {
    private Bundle mBundle;

   private final static String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
   private  ArrayList<Item> arrayList = new ArrayList<>();



    public static void execute(FragmentActivity fa, ITaskLoaderListener taskLoaderListener, Bundle arguments) {
        OpenKinogoPageTask loader = new OpenKinogoPageTask(fa,arguments);

        new TaskProgressDialogFragment.Builder(fa, loader,"Wait...")
                .setCancelable(true)
                .setTaskLoaderListener(taskLoaderListener)
                .show();
    }

    protected OpenKinogoPageTask(Context context,Bundle arguments) {
        super(context);
        if (arguments != null)  this.mBundle=arguments;

    }

    @Override
    public Object loadInBackground() {
        String urlpage = mBundle.getString("urlpage");;
        String forward="";
        String backward="";

        try {
            arrayList.clear();
            Document document = Jsoup.connect(urlpage).userAgent(userAgent).get();

            Elements shortstory = document.select(".shortstory");
            Elements headers = shortstory.select(".zagolovki");
            Elements imgs = shortstory.select(".shortimg");

            for (Element titleFromSite : headers) { //получение названия фильма
                if (titleFromSite.text().equals("")) continue;
                String filmName = titleFromSite.text();
               Element link = titleFromSite.select("a").first();
                String linkHref = link.attr("href");






                Item tempitem = new Item("", "", "", 0, 0, "", "");
                tempitem.mHeader = filmName;
                tempitem.mSubHeader = " ";
                tempitem.mPagelink = linkHref;
                arrayList.add(tempitem);
            }
            Integer i = 0;
            for (Element img : imgs) {   // получение постера и описания
                if (imgs.size() == headers.size()) {
                    String subtext = img.text();
                   // Element link = img.select("a").first();  //большие постеры
                 //   String linkHref = link.attr("href");

                    Element link= img.select("img").first(); //мелкие постеры
                    String linkHref = link.absUrl("src");
                    System.out.println(linkHref);

                    arrayList.get(i).mSubHeader = subtext;
                    arrayList.get(i).mPictureurl = linkHref;
                    i++;
                }
            }
            Elements buttons = document.select(".bot-navigation"); // получение ссылок на кнопки назад-вперед
            for (Element button : buttons) {
                Element link1 = button.select("a").first();
                GlobalData.Gd_back_pagelink = link1.attr("href");
                Element link2 = button.select("a").last();
                GlobalData.Gd_forward_pagelink = link2.attr("href");
            }

            Element span = buttons.select("span").first();
            backward = span.text();


            Element span2 = buttons.select("span").last();
            forward = span2.text();

            if (isCanselled()) {
                //      break;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

        Bundle mbundle = new Bundle();
        mbundle.putSerializable("OpenKinogoPageTask",arrayList);
        mbundle.putString("header","OpenKinogoPageTask");
        if (backward.equalsIgnoreCase("Раньше"))  mbundle.putBoolean("backward_state",false);
           else  mbundle.putBoolean("backward_state",true);

        if (forward.equalsIgnoreCase("Позже"))  mbundle.putBoolean("forward_state",false);
        else  mbundle.putBoolean("forward_state",true);

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
