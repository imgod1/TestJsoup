package com.imgod.gaokang.testjsoup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 文档地址
 * http://www.open-open.com/jsoup/
 */
public class MainActivity extends AppCompatActivity {

    public static final String ImageUrl = "http://www.dbmeinv.com/dbgroup/show.htm?cid=4&pager_offset=1";
    private Button btn_start;
    private TextView txt_content;
    private StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringBuilder = new StringBuilder();
                startParse();
                startParseUrl();
            }
        });
    }

    private void initView() {
        btn_start = (Button) findViewById(R.id.btn_start);
        txt_content = (TextView) findViewById(R.id.txt_content);
    }

    private void startParse() {
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p><p>Parsed HTML into a doc.1</p><p>Parsed HTML into a doc.2</p></body></html>";
        Document doc = Jsoup.parse(html);
        Elements pelements = doc.select("p");
        for (int i = 0; i < pelements.size(); i++) {
            String content = pelements.get(i).text();
            Log.e("start", "p_" + i + ":" + content);
            stringBuilder.append("p_" + i + ":" + content + "\n");
        }
        txt_content.setText(stringBuilder.toString());
        Elements titleelements = doc.select("title");
        for (int i = 0; i < titleelements.size(); i++) {
            String content = titleelements.get(i).text();
            Log.e("start", "title_" + i + ":" + content);
            stringBuilder.append("title_" + i + ":" + content + "\n");
        }
        txt_content.setText(stringBuilder.toString());
    }

    private void startParseUrl() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.e("start", "AsyncTask doInBackground");
                Document doc = null;
                try {
                    doc = Jsoup.connect(ImageUrl).timeout(3000).get();
//                    Elements elements = doc.select("div.thumbnail>div.img_single>a");//得到的是每一个图片所指向的连接
//                      Elements elements = doc.select("div[class=thumbnail]>div[class=img_single]>a");//得到的是每一个图片所指向的连接
                    Elements elements = doc.select("div[class=thumbnail] > div[class=img_single] > a > img");//得到的是每一个图片的地址
                    final int size = elements.size();
                    for (int i = 0; i < size; i++) {
                        String imageUrl = elements.get(i).attr("src");//从该节点取出该属性的值,前两个Elements拿的是href,第三个取src
                        Log.e("start", "position:" + i + "\timageUrl:\t" + imageUrl);
                        stringBuilder.append("position:" + i + "\timageUrl:\t" + imageUrl + "\n");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_content.setText(stringBuilder.toString());
                        }
                    });

                } catch (IOException e) {
                    Log.e("start", "AsyncTask 遇到异常:" + e.getMessage());
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("start", "AsyncTask onPostExecute");
            }
        }.execute();
    }
}
