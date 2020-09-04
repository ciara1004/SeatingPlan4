package ie.wit.seatingplan.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.seatingplan.R
import ie.wit.seatingplan.fragments.AboutUsFragment
import ie.wit.seatingplan.fragments.HomeFragment
import ie.wit.seatingplan.fragments.TableFragment
import ie.wit.seatingplan.fragments.TableLayoutFragment
import ie.wit.seatingplan.main.MainActivity
import ie.wit.seatingplan.signUp_Login.LoginActivity
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class Home : AppCompatActivity(), AnkoLogger,

    NavigationView.OnNavigationItemSelectedListener
{
    lateinit var ft: FragmentTransaction
    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(this.toolbar)

        app = application as MainActivity

        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        ft = supportFragmentManager.beginTransaction()

        val fragment = HomeFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> navigateTo(HomeFragment.newInstance())
            R.id.nav_tablelayout -> navigateTo(TableLayoutFragment.newInstance())
            R.id.nav_tableplan -> navigateTo(TableFragment.newInstance())
            R.id.nav_aboutus -> navigateTo(AboutUsFragment.newInstance())
            R.id.nav_logout -> onLogOut()
            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun onLogOut(){
        FirebaseAuth.getInstance().signOut()
        toast("Logged Out")
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }
}