package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.fragchat.R;

/**
 * Created by LeonardoAlmeida on 02/06/16.
 */
public class ActivityMain extends ActivityDefault {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (findViewById(R.id.content_frame) != null) {
            if (savedInstanceState != null) {
                return;
            }

            navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });

            mDrawerToggle = setupDrawerToggle();
            mDrawerLayout.addDrawerListener(mDrawerToggle);

            FragmentLoginAuth firstFragment = FragmentLoginAuth.newInstance(this);

            firstFragment.setArguments(getIntent().getExtras());

            mFragmentManager.beginTransaction()
                    .add(R.id.content_frame, firstFragment, getString(R.string.login)).commit();

            mToolbarSubTitle.setText(R.string.login);

        }

    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        try {
            return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.abrir_drawer, R.string.fechar_drawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    ImageView _drawerHeader = (ImageView) drawerView.findViewById(R.id.header_cover_image);
                    ImageView _drawerNotConn = (ImageView) drawerView.findViewById(R.id.not_connected_icon);
                    ImageView _drawerProfilePhoto = (ImageView) drawerView.findViewById(R.id.drawer_profile_photo);
                    TextView _drawerName = (TextView) drawerView.findViewById(R.id.user_profile_name);
                    TextView _drawerEmail = (TextView) drawerView.findViewById(R.id.user_profile_email);
                    NavigationView _drawerNav = (NavigationView) drawerView.findViewById(R.id.navView);
                    if (localdb.validateToken(loggedUserToken)) {
                        _drawerNotConn.setVisibility(View.GONE);
                        _drawerProfilePhoto.setVisibility(View.VISIBLE);
                        _drawerProfilePhoto.setImageResource(loggedUserProfile.getImgAvatar());
                        _drawerHeader.setImageResource(loggedUserProfile.getImgHeader());
                        _drawerEmail.setVisibility(View.VISIBLE);
                        _drawerEmail.setText(loggedUserProfile.getEmail());
                        _drawerName.setText(loggedUserProfile.getNome());
                        _drawerNav.getMenu().setGroupVisible(0, true);

                    } else {
                        _drawerNotConn.setVisibility(View.VISIBLE);
                        _drawerProfilePhoto.setVisibility(View.GONE);
                        _drawerProfilePhoto.setImageResource(R.drawable.profile_demo_4);
                        _drawerHeader.setImageResource(R.drawable.profile_header5);
                        _drawerEmail.setVisibility(View.GONE);
                        _drawerEmail.setText(R.string.email_user);
                        _drawerName.setText(R.string.disconnected);
                        _drawerNav.getMenu().setGroupVisible(0, false);
                    }
                    super.onDrawerOpened(drawerView);
                }
            };
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {

        if (loggedUserProfile == null) {
            Toast.makeText(getApplicationContext(), "Usuário não conectado!!", Toast.LENGTH_SHORT).show();
            // Close the navigation drawer
            mDrawerLayout.closeDrawers();

            return;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        menuItem.setChecked(true);

        mFragmentManager.popBackStack();

        // Close the navigation drawer
        mDrawerLayout.closeDrawers();

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        int tag = 0;
        switch(menuItem.getItemId()) {
            case R.id.nav_profile:
                tag = R.string.profile;
                fragment = FragmentProfile.newInstance(this, FragmentProfile.PROFILE_MAINUSER);
                break;
            case R.id.nav_messenger:
                tag = R.string.messenger;
                fragment = FragmentMessenger.newInstance(this);
                break;
            case R.id.nav_update:
                tag = R.string.register;
                fragment = FragmentSignup.newInstance(this);
                break;
            case R.id.nav_logout:
                loggedUserToken = null;
                loggedUserProfile = null;
                Intent intent = new Intent(ActivityMain.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return;
            default:
                tag = R.string.login;
                fragment = FragmentLoginAuth.newInstance(this);
        }

        if (!fragment.isAdded()) {
            long userid = loggedUserProfile.getId();
            Bundle dataBundle = new Bundle();
            dataBundle.putLong("id", userid);
            if (loggedUserToken != null) dataBundle.putLong("tokenid", loggedUserToken.getId());
            fragment.setArguments(dataBundle);
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        transaction.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        // mFragmentManager.beginTransaction()
         //        .add(R.id.content_frame, fragment, getString(tag)).addToBackStack(null).commit();

        // Set action bar title
        setTitle(menuItem.getTitle());
    }

    @Override
    public void onBackPressed() {

        int count = mFragmentManager.getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();

            // Disable going back to the MainActivity
            moveTaskToBack(true);
        } else {
            mFragmentManager.popBackStack();
        }

    }

}
