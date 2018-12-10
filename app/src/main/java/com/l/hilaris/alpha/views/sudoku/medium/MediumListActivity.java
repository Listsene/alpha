package com.l.hilaris.alpha.views.sudoku.medium;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.l.hilaris.alpha.R;
import com.l.hilaris.alpha.views.sudoku.hard.HardListFragment;

public class MediumListActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_list);

        String mode = (String) getIntent().getSerializableExtra("mode");
        getIntent().putExtra("mode", mode);

        // Opens TestListFragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.MediumListFragment, new MediumListFragment());
        ft.commit();
    }
}
