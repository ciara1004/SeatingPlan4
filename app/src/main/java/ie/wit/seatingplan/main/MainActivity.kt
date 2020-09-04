package ie.wit.seatingplan.main

import android.app.Application
import android.util.Log
import com.google.firebase.database.DatabaseReference
import ie.wit.seatingplan.models.PlanStore
import org.jetbrains.anko.AnkoLogger


class MainActivity : Application(), AnkoLogger {

  lateinit var database: DatabaseReference
  lateinit var planStore: PlanStore

  override fun onCreate() {
    super.onCreate()

    Log.v("Plan","Seating Plan App started")

  }
}