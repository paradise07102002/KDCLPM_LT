package edu.huflit.kdclpm_lt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import edu.huflit.kdclpm_lt.DauSach.AddDauSach;
import edu.huflit.kdclpm_lt.DauSach.MH_DauSach;
import edu.huflit.kdclpm_lt.DauSach.UpdateDauSach;
import edu.huflit.kdclpm_lt.DocGia.AddDocGia;
import edu.huflit.kdclpm_lt.DocGia.DetailDocGia;
import edu.huflit.kdclpm_lt.DocGia.MH_DocGia;
import edu.huflit.kdclpm_lt.DocGia.UpdateDocGia;
import edu.huflit.kdclpm_lt.MuonTraSach.AddMuonTra;
import edu.huflit.kdclpm_lt.MuonTraSach.MH_MuonTra;
import edu.huflit.kdclpm_lt.NhanVien.Add_NhanVien;
import edu.huflit.kdclpm_lt.NhanVien.DetailNhanVien;
import edu.huflit.kdclpm_lt.NhanVien.MH_NhanVien;
import edu.huflit.kdclpm_lt.NhanVien.UpdateNhanVien;
import edu.huflit.kdclpm_lt.Object.User;
import edu.huflit.kdclpm_lt.SQLite.DBHelper;
import edu.huflit.kdclpm_lt.SQLite.MyDatabase;
import edu.huflit.kdclpm_lt.Sach.AddSach;
import edu.huflit.kdclpm_lt.Sach.DetailSach;
import edu.huflit.kdclpm_lt.Sach.MH_Sach;
import edu.huflit.kdclpm_lt.Sach.UpdateSach;
import edu.huflit.kdclpm_lt.TaiKhoan.Doi_MK;
import edu.huflit.kdclpm_lt.TaiKhoan.TaiKhoan;
import edu.huflit.kdclpm_lt.TaiKhoan.Update_TK;

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
                if (itemId == R.id.action_tai_khoan)
                {
                    replaceFragment(new TaiKhoan());
                }
                return true;
            }
        });
    }
    public void anhXa()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.hdrawer_layout);
        toolbar = (Toolbar) findViewById(R.id.htoolbar);
        navigationView = (NavigationView) findViewById(R.id.hnavigation_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
    }

    //ITEM TRÊN NAVIGATION
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_ql_dau_sach)
        {
            replaceFragment(new MH_DauSach());
        } else if (id == R.id.nav_ql_sach) {
            replaceFragment(new MH_Sach());
        } else if (id == R.id.nav_ql_doc_gia) {
            replaceFragment(new MH_DocGia());
        } else if (id == R.id.nav_ql_muon_tra) {
            replaceFragment(new MH_MuonTra());
        } else if (id == R.id.nav_ql_nhan_vien) {
            replaceFragment(new MH_NhanVien());
        } else if (id == R.id.nav_dang_xuat) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ManHinhChinh.this);
            builder.setTitle("Bạn muốn đăng xuất");
            builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", null);
                    editor.putBoolean("is_login", false);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), DangNhap.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
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
    //CHUYỂN FRAGMENT
    //ĐẦU SÁCH
    public void nextQLDauSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MH_DauSach mh_dauSach = new MH_DauSach();

        fragmentTransaction.replace(R.id.hcontent_frame, mh_dauSach);
        fragmentTransaction.commit();
    }
    public void nextAddDauSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddDauSach addDauSach = new AddDauSach();

        fragmentTransaction.replace(R.id.hcontent_frame, addDauSach);
        fragmentTransaction.commit();
    }
    public void nextUpdateDauSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UpdateDauSach updateDauSach = new UpdateDauSach();

        fragmentTransaction.replace(R.id.hcontent_frame, updateDauSach);
        fragmentTransaction.commit();
    }
    public void nextQLSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MH_Sach mh_sach = new MH_Sach();

        fragmentTransaction.replace(R.id.hcontent_frame, mh_sach);
        fragmentTransaction.commit();
    }
    public void nextAddSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddSach addSach = new AddSach();

        fragmentTransaction.replace(R.id.hcontent_frame, addSach);
        fragmentTransaction.commit();
    }
    public void nextUpdateSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UpdateSach updateSach = new UpdateSach();

        fragmentTransaction.replace(R.id.hcontent_frame, updateSach);
        fragmentTransaction.commit();
    }
    public void nextDetailSach()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailSach detailSach = new DetailSach();

        fragmentTransaction.replace(R.id.hcontent_frame, detailSach);
        fragmentTransaction.commit();
    }
    public void nextAddDocGia()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddDocGia addDocGia = new AddDocGia();

        fragmentTransaction.replace(R.id.hcontent_frame, addDocGia);
        fragmentTransaction.commit();
    }
    public void nextQLDocGia()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MH_DocGia mhDocGia = new MH_DocGia();

        fragmentTransaction.replace(R.id.hcontent_frame, mhDocGia);
        fragmentTransaction.commit();
    }
    public void nextQLDetailDocGia()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailDocGia detailDocGia = new DetailDocGia();

        fragmentTransaction.replace(R.id.hcontent_frame, detailDocGia);
        fragmentTransaction.commit();
    }
    public void nextUpdateDocGia()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UpdateDocGia updateDocGia = new UpdateDocGia();

        fragmentTransaction.replace(R.id.hcontent_frame, updateDocGia);
        fragmentTransaction.commit();
    }
    public void nextAddMuonTra()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AddMuonTra addMuonTra = new AddMuonTra();

        fragmentTransaction.replace(R.id.hcontent_frame, addMuonTra);
        fragmentTransaction.commit();
    }
    public void nextQLMuonTra()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MH_MuonTra mhMuonTra = new MH_MuonTra();

        fragmentTransaction.replace(R.id.hcontent_frame, mhMuonTra);
        fragmentTransaction.commit();
    }
    public void nextDoiMK()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Doi_MK doiMk = new Doi_MK();
        fragmentTransaction.replace(R.id.hcontent_frame, doiMk);
        fragmentTransaction.commit();
    }
    public void nextTaiKhoan()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        TaiKhoan taiKhoan = new TaiKhoan();
        fragmentTransaction.replace(R.id.hcontent_frame, taiKhoan);
        fragmentTransaction.commit();
    }
    public void nextUpdateTK()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Update_TK updateTk = new Update_TK();
        fragmentTransaction.replace(R.id.hcontent_frame, updateTk);
        fragmentTransaction.commit();
    }
    public void nextAddNhanVien()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Add_NhanVien addNhanVien = new Add_NhanVien();
        fragmentTransaction.replace(R.id.hcontent_frame, addNhanVien);
        fragmentTransaction.commit();
    }
    public void nextMHNhanVien()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MH_NhanVien mhNhanVien = new MH_NhanVien();
        fragmentTransaction.replace(R.id.hcontent_frame, mhNhanVien);
        fragmentTransaction.commit();
    }
    public void nextUpdateNhanVien()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UpdateNhanVien updateNhanVien = new UpdateNhanVien();
        fragmentTransaction.replace(R.id.hcontent_frame, updateNhanVien);
        fragmentTransaction.commit();
    }public void nextDetailNhanVien()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailNhanVien detailNhanVien = new DetailNhanVien();
        fragmentTransaction.replace(R.id.hcontent_frame, detailNhanVien);
        fragmentTransaction.commit();
    }

}