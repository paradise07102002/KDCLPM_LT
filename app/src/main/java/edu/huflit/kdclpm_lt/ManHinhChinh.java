package edu.huflit.kdclpm_lt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;

public class ManHinhChinh extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    public static NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    MyDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chinh);
        setTitle("             Quản Lý Thư Viện");
        database = new MyDatabase(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        anhXa();


        SharedPreferences get_user = getSharedPreferences("login", MODE_PRIVATE);
        String username = get_user.getString("username", null);
        boolean is_login = get_user.getBoolean("is_login", false);

        //LẤY USER NAME EMAIL SHOW LÊN HEADER
        TextView show_username = navigationView.getHeaderView(0).findViewById(R.id.username1);
        TextView show_email = navigationView.getHeaderView(0).findViewById(R.id.email1);
        Cursor cursor = database.getUserByUsername(username);

        int username_index = cursor.getColumnIndex(DBHelper.USERNAME_USER);
        int email_index = cursor.getColumnIndex(DBHelper.EMAIL_USER);
        cursor.moveToFirst();

        show_username.setText(cursor.getString(username_index));
        show_email.setText(cursor.getString(email_index));
        cursor.close();

        //ADD Toolbar
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,   R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_admin_1)
                {

                }
                return true;
            }
        });
        //Kiểm tra đăng nhập, nếu chưa thì hiện đăng nhập, ngược lại hiện đăng xuất trên menu
        //SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        Menu navigationMenu = navigationView.getMenu();
        MenuItem menuItem = navigationMenu.findItem(R.id.nav_tittle6);
        boolean check_login = sharedPreferences.getBoolean("is_login", false);
        if (check_login == false)
        {
            menuItem.setTitle("Đăng nhập");
        }
        else
        {
            menuItem.setTitle("Đăng xuất");
        }
    }
    public void anhXa()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.hdrawer_layout);
        toolbar = (Toolbar) findViewById(R.id.htoolbar);
        navigationView = (NavigationView) findViewById(R.id.hnavigation_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_tittle1)
        {

        }

        //Đóng drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Nếu drawer đang ở thì khi ấn back sẽ back
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();//ngược lại thì cho thoát áp
        }
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.hcontent_frame, fragment);
        transaction.commit();
    }
}