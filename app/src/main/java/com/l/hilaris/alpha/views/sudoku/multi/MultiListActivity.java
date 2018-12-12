package com.l.hilaris.alpha.views.sudoku.multi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.views.sudoku.easy.EasyListFragment;
import com.l.hilaris.alpha.views.sudoku.multi.MultiListFragment;

public class MultiListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_list);

        String mode = (String) getIntent().getSerializableExtra("mode");
        getIntent().putExtra("mode", mode);

        // Opens TestListFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.MultiListFragment, new MultiListActivity());
        ft.commit();
    }
}