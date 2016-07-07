package com.daniloprado.weather.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.daniloprado.weather.R;
import com.daniloprado.weather.model.City;
import com.daniloprado.weather.view.base.BaseActivity;
import com.daniloprado.weather.view.base.ContractFragment;
import com.daniloprado.weather.view.cityadd.CityAddDialogFragment;
import com.daniloprado.weather.view.cityforecast.CityForecastFragment;
import com.daniloprado.weather.view.citylist.CityListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements CityListFragment.Contract, FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDiComponent().inject(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        init();

        if (savedInstanceState == null) {
            setupCityListFragment();
        }
    }

    private void init() {
        initToolbar();
    }

    private void setupCityListFragment() {
        addContentFragment(new CityListFragment());
    }

    private void setupCityForecastFragment(City city) {
        CityForecastFragment cityForecastFragment = new CityForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        cityForecastFragment.setArguments(args);
        replaceContentFragment(cityForecastFragment);
    }

    private void replaceContentFragment(ContractFragment contractFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_content, contractFragment)
                .addToBackStack(null)
                .commit();
    }

    private void addContentFragment(ContractFragment contractFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root_content, contractFragment)
                .commit();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayShowTitleEnabled(true);
            }
            shouldDisplayHomeUp();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onCitySelected(City city) {
        setupCityForecastFragment(city);
    }

    @Override
    public void onCityAddDialogDismissed() {
        initToolbar();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        boolean enableHomeUp = getSupportFragmentManager().getBackStackEntryCount() > 0;
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(enableHomeUp);
        }
    }
}
