package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konekin.databinding.ActivityMainBinding
import com.example.konekin.model.Data
import com.example.konekin.model.Users
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()
        val response = client.getAllUsers()
        val userList = ArrayList<String>()
        val userListData = ArrayList<Data>() // Menampung objek Data lengkap untuk mengambil ID

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful && response.body() != null) {
                    val dataFromApi = response.body()!!.data

                    // 1. Simpan semua objek data ke list agar ID-nya tidak hilang
                    userListData.addAll(dataFromApi)

                    // 2. Ambil nama untuk ditampilkan di ListView
                    for (i in dataFromApi) {
                        userList.add(i.employeeName)
                    }

                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        userList
                    )
                    binding.lvNama.adapter = listAdapter
                }
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi error",
                    Toast.LENGTH_LONG).show()
            }
        })

        // 3. Tambahkan event listener saat item ListView diklik
        binding.lvNama.setOnItemClickListener { _, _, position, _ ->
            if (userListData.isNotEmpty()) {
                val selectedUser = userListData[position]

                // Buka UserDetailActivity sambil membawa data ID
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra("EXTRA_ID", selectedUser.id)
                startActivity(intent)
            }
        }

    }
}