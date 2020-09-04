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
import kotlinx.android.synthetic.main.edit_tablelayout_fragment.view.*
import kotlinx.android.synthetic.main.fragment_tablelayout.view.*
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest01
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest02
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest03
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest04
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest05
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest06
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest07
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest08
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest09
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest10
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest11
import kotlinx.android.synthetic.main.fragment_tablelayout.view.guest12
import kotlinx.android.synthetic.main.fragment_tablelayout.view.tableNo
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap

class EditTableFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity

    val planAuth = FirebaseAuth.getInstance()
    var editPlan: PlanModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app=activity?.application as MainActivity
        arguments?.let { editPlan = it.getParcelable("editPlan") }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val editTableFragment = inflater.inflate(R.layout.edit_tablelayout_fragment, container, false)
        activity?.title = getString(R.string.action_tablelayout)

        editTableFragment.saveEditButton.setOnClickListener{
            updatePlanDetails(editTableFragment)
            updatePlan(editPlan!!.id, editPlan!!)
            updateUserTables(planAuth.currentUser!!.uid,
                editPlan!!.id, editPlan!!)
        }

        editTableFragment.tableNo_new.setText(editPlan!!.tableNo)
        editTableFragment.guest01_new.setText(editPlan!!.guest01)
        editTableFragment.guest02_new.setText(editPlan!!.guest02)
        editTableFragment.guest03_new.setText(editPlan!!.guest03)
        editTableFragment.guest04_new.setText(editPlan!!.guest04)
        editTableFragment.guest05_new.setText(editPlan!!.guest05)
        editTableFragment.guest06_new.setText(editPlan!!.guest06)
        editTableFragment.guest07_new.setText(editPlan!!.guest07)
        editTableFragment.guest08_new.setText(editPlan!!.guest08)
        editTableFragment.guest09_new.setText(editPlan!!.guest09)
        editTableFragment.guest10_new.setText(editPlan!!.guest10)
        editTableFragment.guest11_new.setText(editPlan!!.guest11)
        editTableFragment.guest11_new.setText(editPlan!!.guest12)

        return editTableFragment
    }

    fun updatePlanDetails(layout: View){

        editPlan!!.tableNo = Integer.parseInt(layout.tableNo.text.toString())
        editPlan!!.guest01 = layout.guest01_new.text.toString()
        editPlan!!.guest02 = layout.guest02_new.text.toString()
        editPlan!!.guest03 = layout.guest03_new.text.toString()
        editPlan!!.guest04 = layout.guest04_new.text.toString()
        editPlan!!.guest05 = layout.guest05_new.text.toString()
        editPlan!!.guest06 = layout.guest06_new.text.toString()
        editPlan!!.guest07 = layout.guest07_new.text.toString()
        editPlan!!.guest08 = layout.guest08_new.text.toString()
        editPlan!!.guest09 = layout.guest09_new.text.toString()
        editPlan!!.guest10 = layout.guest10_new.text.toString()
        editPlan!!.guest11 = layout.guest11_new.text.toString()
        editPlan!!.guest12 = layout.guest12_new.text.toString()
    }

    private fun updatePlan(uid: String?, plan: PlanModel) {
        app.database.child("tables").child(uid!!.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(plan)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase error : ${error.message}")
                    }
                })
    }

    private fun updateUserTables(userId: String, uid: String?, plan: PlanModel) {
        app.database.child("user-tables").child(userId).child(uid!!.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(plan)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, TableFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase error : ${error.message}")
                    }
                })
    }

    companion object {
        @JvmStatic
        fun newInstance(plan: PlanModel) =
            EditTableFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("edit_plan", plan) }
            }
    }

}