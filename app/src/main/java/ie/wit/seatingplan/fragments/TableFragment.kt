package ie.wit.seatingplan.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.seatingplan.utils.SwipeToDeleteCallback
import ie.wit.seatingplan.R
import ie.wit.seatingplan.adapters.PlanAdapter
import ie.wit.seatingplan.adapters.TableListener
import ie.wit.seatingplan.main.MainActivity
import ie.wit.seatingplan.models.PlanModel
import ie.wit.seatingplan.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.fragment_table.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class TableFragment : Fragment(), AnkoLogger, TableListener {

    lateinit var app: MainActivity
    lateinit var tableFragment: View
    val planAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tableFragment = inflater.inflate(R.layout.fragment_table, container, false)
        tableFragment.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        val planAuth = FirebaseAuth.getInstance()

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = tableFragment.recyclerView.adapter as PlanAdapter

                adapter.removeAt(viewHolder.adapterPosition)
                deleteTable((viewHolder.itemView.tag as PlanModel).id)
                deleteUserTable(planAuth.currentUser!!.uid,
                    (viewHolder.itemView.tag as PlanModel).id)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(tableFragment.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onTableListener(viewHolder.itemView.tag as PlanModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(tableFragment.recyclerView)

        getTables(planAuth.currentUser!!.uid)
        return tableFragment
    }

    fun setSwipeRefresh() {
        tableFragment.swiperefresh.setOnRefreshListener {
            tableFragment.swiperefresh.isRefreshing = true
            getTables(planAuth.currentUser!!.uid)
        }
    }

    fun checkSwipeRefresh() {
        if (tableFragment.swiperefresh.isRefreshing) tableFragment.swiperefresh.isRefreshing = false
    }

    override fun onTableListener(plan: PlanModel) {

        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditTableFragment.newInstance(plan))
            .addToBackStack(null)
            .commit()
    }

    fun deleteUserTable(userId: String, uid:String?) {
        app.database.child("user-tables").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase error : ${error.message}")
                    }
                })
    }

    fun deleteTable(uid: String?) {
        app.database.child("tables").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase error : ${error.message}")
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        val planAuth = FirebaseAuth.getInstance()
        getTables(planAuth.currentUser!!.uid)
    }

    private fun getTables(userId:String)
    {
        val tables = ArrayList<PlanModel>()
        app.database.child("user-tables").child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    children.forEach{
                        val table = it.getValue(PlanModel::class.java)

                        tables.add(table!!)

                        tableFragment.recyclerView.adapter =
                            PlanAdapter(tables, this@TableFragment)
                        tableFragment.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                        app.database.child("user-tables").child(userId).removeEventListener(this)
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TableFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}