package pt.ipp.estg.pdm_tp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    private Fragment mFragment;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomnavigation();
        mFragment = new FragmentMap();

        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, mFragment);
        fr.addToBackStack(null);
        fr.commit();

    }

    public void initBottomnavigation() {

        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.baseline_home_black_18dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.baseline_add_location_black_18dp, "Adicionar Local"))
                .addItem(new BottomNavigationItem(R.drawable.baseline_location_city_black_18dp, "Sitios"))
                .addItem(new BottomNavigationItem(R.drawable.baseline_favorite_black_18dp, "Favoritos"))
                .addItem(new BottomNavigationItem(R.drawable.baseline_directions_black_18dp, "Rotas"))
                .setFirstSelectedPosition(0)
                .initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0) {
                    mFragment = new FragmentMap();
                    // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 1) {
                    mFragment = new AddLocals();
                    // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 2) {
                    mFragment = new List_Locals_I();
                    // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 3) {
                    mFragment = new List_Locals_Favourites();
                    // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 4) {
                    mFragment = new Navigation();
                    // startActivity(new Intent(MainActivity.this, FragmentMap.class));
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }


}
