/*
 * Copyright (c) 2019 Yoganandh Durvasulu. All rights reserved.
 */

package co.ld.codechallenge;

import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.navigation.fragment.NavHostFragment;
import co.ld.codechallenge.common.BaseActivity;

/**
 * Only screen acts as interaction layer between user and device
 */
public class MainActivity extends BaseActivity {

    @LayoutRes
    @Override
    protected int getLayout() {
        Log.d("YESS", "1");
        // Return current layout
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        Log.d("YESS", "2");
        // Get Navigation Graph
        NavHostFragment host = NavHostFragment.create(R.navigation.nav_graph);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, host)
                .setPrimaryNavigationFragment(host)
                .commit();
    }
}
