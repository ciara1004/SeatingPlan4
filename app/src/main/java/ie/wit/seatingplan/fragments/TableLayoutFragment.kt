package ie.wit.seatingplan.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.seatingplan.R
import ie.wit.seatingplan.main.MainActivity
import ie.wit.seatingplan.models.PlanModel
import kotlinx.android.synthetic.main.fragment_tablelayout.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap

class TableLayoutFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app=activity?.application as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val tableLayoutFragment = inflater.inflate(R.layout.fragment_tablelayout, container, false)
        activity?.title = getString(R.string.action_tablelayout)

        setButtonListener(tableLayoutFragment)

        return tableLayoutFragment
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TableLayoutFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun setButtonListener(layout: View) {
        layout.saveButton.setOnClickListener {

            val tableNumber = Integer.parseInt(layout.tableNo.text.toString())
            val guest1 = layout.guest01.text.toString()
            val guest2 = layout.guest02.text.toString()
            val guest3 = layout.guest03.text.toString()
            val guest4 = layout.guest04.text.toString()
            val guest5 = layout.guest05.text.toString()
            val guest6 = layout.guest06.text.toString()
            val guest7 = layout.guest07.text.toString()
            val guest8 = layout.guest08.text.toString()
            val guest9 = layout.guest09.text.toString()
            val guest10 = layout.guest10.text.toString()
            val guest11 = layout.guest11.text.toString()
            val guest12 = layout.guest12.text.toString()

            writeTablePlan(
                PlanModel(
                    tableNo = tableNumber,
                    guest01 = guest1,
                    guest02 = guest2,
                    guest03 = guest3,
                    guest04 = guest4,
                    guest05 = guest5,
                    guest06 = guest6,
                    guest07 = guest7,
                    guest08 = guest8,
                    guest09 = guest9,
                    guest10 = guest10,
                    guest11 = guest11,
                    guest12 = guest12
                )
            )

            var fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.homeFrame, TableFragment())
            fr?.commit()
        }
    }

    private fun writeTablePlan(plan: PlanModel){
        val planAuth = FirebaseAuth.getInstance()
        val id = planAuth.currentUser!!.uid
        val key = app.database.child("Tables").push().key
        plan.id = key
        val userValues = plan.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/Tables/$id/$key"]=userValues
        app.database.updateChildren(childUpdates)
    }
}