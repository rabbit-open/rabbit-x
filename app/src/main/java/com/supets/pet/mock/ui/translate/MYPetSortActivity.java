package com.supets.pet.mock.ui.translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.supets.pet.mock.db.WordDao;
import com.supets.pet.mockui.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class MYPetSortActivity extends FragmentActivity implements SideBar.OnTouchingLetterChangedListener, AdapterView.OnItemClickListener {

    private SideBar sideBar;
    private ListView sortListView;
    private SortAdapter adapter;

    private WordDao wordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_android);

        wordDao = new WordDao(this);

        sortListView = findViewById(R.id.country_lvcountry);
        sideBar = findViewById(R.id.sidrbar);
        TextView tip = findViewById(R.id.dialog);
        sideBar.setTextView(tip);
        sideBar.setOnTouchingLetterChangedListener(this);
        sortListView.setOnItemClickListener(this);

        adapter = new SortAdapter(this);
        sortListView.setAdapter(adapter);

        findall();

        EditText input = findViewById(R.id.input);
        findViewById(R.id.search).setOnClickListener(view -> {
            String words = input.getText().toString();
            if (!TextUtils.isEmpty(words)) {
                requestData(words.toLowerCase());
            } else {
                findall();
            }
        });

    }

    private void findall() {
        ArrayList<SortModel> datas=wordDao.select();
        Collections.sort(datas, new PinyinComparator());
        Log.v("words total:","=="+datas.size());
        adapter.updateListView(datas);
    }

    private void requestData(String s) {
        ArrayList<SortModel>  datas=wordDao.selectName(s);
        Collections.sort(datas, new PinyinComparator());
        adapter.updateListView(datas);
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            sortListView.setSelection(position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SortModel model = (SortModel) parent.getAdapter().getItem(position);
        if (model != null) {
            Intent intent = new Intent(this, TranslateActivity.class);
            intent.putExtra("content", model.name);
            startActivity(intent);
        }
    }
}
