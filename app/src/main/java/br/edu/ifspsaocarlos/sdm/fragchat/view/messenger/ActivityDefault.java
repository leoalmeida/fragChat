package br.edu.ifspsaocarlos.sdm.fragchat.view.messenger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.fragchat.R;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserModel;
import br.edu.ifspsaocarlos.sdm.fragchat.models.UserTokenModel;
import br.edu.ifspsaocarlos.sdm.fragchat.service.MessengerContactsService;
import br.edu.ifspsaocarlos.sdm.fragchat.service.MessengerReceiverService;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.DBHelper;
import br.edu.ifspsaocarlos.sdm.fragchat.utils.TouchEffect;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityAsyncTaskWS;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityMensagemHandler;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityRunOnUIThread;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityRunnableHandler;
import br.edu.ifspsaocarlos.sdm.fragchat.view.otherActivities.ActivityVolleyMessageHandler;

/**
 * Created by LeonardoAlmeida on 18/07/16.
 */

public class ActivityDefault extends AppCompatActivity implements View.OnClickListener
{

    public static final short NEW_CONTACT_SERVICE = 1;
    public static final short RECEIVER_SERVICE = 2;
    public static final short SENDER_SERVICE = 3;

    /**
     * Apply this Constant as touch listener for views to provide alpha touch
     * effect. The view must have a Non-Transparent background.
     */
    public static final TouchEffect TOUCH = new TouchEffect();

    protected DrawerLayout mDrawerLayout;
    protected Toolbar toolbar;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected NavigationView navDrawer;
    protected TextView mToolbarSubTitle;
    protected ProgressBar mToolbarProgress;
    protected ImageView mToolbarProgressOK;

    protected NotificationManager mNotificationManager;
    protected FragmentManager mFragmentManager;
    protected DBHelper localdb;

    public static UserModel loggedUserProfile;
    protected static UserTokenModel loggedUserToken;
    protected static ArrayList<UserModel> contactList;
    public long selectedContactId = 0;


    private Intent newContactsServiceIntent, msgReceiverServiceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navDrawer = (NavigationView) findViewById(R.id.navView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToolbarProgress = (ProgressBar) findViewById(R.id.nav_pb_carregando);
        mToolbarProgressOK = (ImageView)  findViewById(R.id.nav_pb_ok);
        mToolbarSubTitle = (TextView) findViewById(R.id.nav_subtitle);

        setSupportActionBar(toolbar);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mFragmentManager = getSupportFragmentManager();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        localdb = getLocaldb();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        newContactsServiceIntent = new Intent(this, MessengerContactsService.class);
        startService(NEW_CONTACT_SERVICE, 0);
        msgReceiverServiceIntent = new Intent(this, MessengerReceiverService.class);

    }

    protected boolean startService(short serviceid, long selectedContactId) {
        this.selectedContactId = selectedContactId;
        switch (serviceid){
            case NEW_CONTACT_SERVICE:
                // newContactsServiceIntent.putExtra(MessengerContactsService.LAST_CONTACT_ID, localdb.getLastContactID());
                startService(newContactsServiceIntent);
                break;
            case RECEIVER_SERVICE:
            default:
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.SENDER, loggedUserProfile.getId());
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.RECEIVER, selectedContactId);
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.LAST_SENT_MSG, localdb.getLastMessageID(loggedUserProfile.getId(), selectedContactId));
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.LAST_RECV_MSG, localdb.getLastMessageID(selectedContactId, loggedUserProfile.getId()));
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.TOKENID, loggedUserToken.getId());
                msgReceiverServiceIntent.putExtra(MessengerReceiverService.TOKEN, loggedUserToken.getToken());
                startService(msgReceiverServiceIntent);
        }

        return true;
    }

    protected boolean stopService(short serviceid) {
        switch (serviceid){
            case NEW_CONTACT_SERVICE:
                stopService(newContactsServiceIntent);
                break;
            case RECEIVER_SERVICE:
            default:
                stopService(msgReceiverServiceIntent);
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return selectMenuItem(menuItem) || super.onOptionsItemSelected(menuItem);
    }

    private boolean selectMenuItem(MenuItem menuItem) {

        if (menuItem == null) {
            Toast.makeText(getApplicationContext(), "Erro!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if(menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_asynctaskws:
                intent = new Intent(this, ActivityAsyncTaskWS.class);
                startActivity(intent);
                break;
            case R.id.nav_msgtests:
                intent = new Intent(this, ActivityMensagemHandler.class);
                startActivity(intent);
                break;
            case R.id.nav_runnable:
                intent = new Intent(this, ActivityRunnableHandler.class);
                startActivity(intent);
                break;
            case R.id.nav_uithread:
                intent = new Intent(this, ActivityRunOnUIThread.class);
                startActivity(intent);
                break;
            case R.id.nav_volley:
                intent = new Intent(this, ActivityVolleyMessageHandler.class);
                startActivity(intent);
                break;
            case R.id.nav_exit:
            default:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        mNotificationManager.cancelAll();
        this.localdb = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        localdb = new DBHelper(this);
    }

    public void showProgressBar(){
        mToolbarProgress.setIndeterminate(true);
        mToolbarProgress.setVisibility(View.VISIBLE);
    }

    public void dismissProgressBar(){
        mToolbarProgressOK.animate().setDuration(700)
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mToolbarProgress.setVisibility(View.GONE);
                        mToolbarProgressOK.animate().setDuration(500)
                                .translationY(0)
                                .alpha(0.0f);
                    }
                });
        mToolbarProgress.setVisibility(View.GONE);
    }

    public void setLoggedUser(UserModel loggedUserProfile, UserTokenModel loggedUserToken){
        this.loggedUserProfile = loggedUserProfile;
        this.loggedUserToken = loggedUserToken;
    }

    public UserTokenModel getLoggedUserToken(){
        return this.loggedUserToken;
    }

    public void setLoggedUserProfile(long userid){
        this.loggedUserProfile = localdb.getContactProfile(userid);
    }

    public UserModel getLoggedUserProfile(){ return this.loggedUserProfile; }

    public void setContactsList(ArrayList<UserModel> contactsList){
        this.contactList = contactsList;
    }

    public ArrayList<UserModel> getContactsList(){
        return this.contactList;
    }

    public DBHelper getLocaldb(){
        return this.localdb;
    }


    /**
     * Sets the click listener for a view with given id.
     *
     * @param id
     *            the id
     * @return the view on which listener is applied
     */
    public View setClick(int id)
    {

        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {

    }

    public long getSelectedContact() {
        return selectedContactId;
    }
}