package com.example.whatsapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupsFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private lateinit var groupFragmentView : View
    private lateinit var list_View : ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val list_of_groups = ArrayList<String>()
    private lateinit var GroupRef: DatabaseReference

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false)

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups")

        initializeFields()
        RetrieveAndDisplayGroups()

        list_View.setOnItemClickListener { adapterView, view, position, id ->
            val currentGroupName = adapterView.getItemAtPosition(position).toString()
            val groupChatIntent = Intent(context,GroupChatActivity::class.java)
            groupChatIntent.putExtra("groupName",currentGroupName)
            startActivity(groupChatIntent)
        }

        return groupFragmentView
    }




    private fun initializeFields() {
        list_View = groupFragmentView.findViewById(R.id.list_view)
        arrayAdapter = ArrayAdapter(context ?: requireContext(), android.R.layout.simple_list_item_1, list_of_groups)
        list_View.setAdapter(arrayAdapter)
    }

    private fun RetrieveAndDisplayGroups() {
        GroupRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val set = HashSet<String>()
                val iterator = dataSnapshot.children.iterator()
                while (iterator.hasNext()) {
                    val childSnapshot = iterator.next() as DataSnapshot
                    set.add(childSnapshot.key ?: "")
                }

                list_of_groups.clear()
                list_of_groups.addAll(set)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Ocurrió un error al leer la base de datos
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GroupsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}