package com.example.firstapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.navigation.NavigationBarView


class BlankFragment : Fragment() {

    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }


    override fun onResume() {
        super.onResume()


        var idButton: Button? = activity?.findViewById(R.id.identity)

        button?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "This is the fragment", Toast.LENGTH_LONG).show()
        })


        idButton?.setOnClickListener(View.OnClickListener {
            val id = arguments?.getString("userId")

            Toast.makeText(context, id.toString(), Toast.LENGTH_LONG).show()

        })

        (activity as MainActivity).bottomNavigationView!!.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                val id = item.itemId
                when (id) {
                    R.id.connect -> {
                        val intent = Intent(context, BluetoothConnection::class.java)
                        startActivity(intent)
                    }

                    R.id.home -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            })

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}