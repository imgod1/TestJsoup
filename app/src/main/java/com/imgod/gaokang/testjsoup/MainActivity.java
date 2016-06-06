package com.imgod.gaokang.testjsoup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String ImageUrl = "http://www.dbmeinv.com/dbgroup/show.htm?cid=4&pager_offset=1";
    private TextView txt_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        txt_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startParse();
                startParseUrl();
            }
        });
    }

    private void initView() {
        txt_main = (TextView) findViewById(R.id.txt_main);
    }

    private void startParse() {
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
        Elements pelements = doc.select("p");
        for (int i = 0; i < pelements.size(); i++) {
            Log.e("start", "text:" + pelements.get(0).text());
        }
        Elements titleelements = doc.select("title");
        for (int i = 0; i < titleelements.size(); i++) {
            Log.e("start", "text:" + titleelements.get(0).text());
        }

        txt_main.setText("title:" + doc.title() + "\nnodeName:" + doc.nodeName());
    }

    private void startParseUrl() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.e("start", "AsyncTask doInBackground");
                Document doc = null;
                try {
                    doc = Jsoup.connect(ImageUrl).get();
                } catch (IOException e) {
                    Log.e("start", "AsyncTask 遇到异常");
                    e.printStackTrace();
                }
                Elements pelements = doc.select("img_single");
                for (int i = 0; i < pelements.size(); i++) {
                    Log.e("start", "text:" + pelements.get(0).text());
                }
                return null;
            }
        }.execute();
    }


}
