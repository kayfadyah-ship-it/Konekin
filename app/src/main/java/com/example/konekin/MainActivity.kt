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

        val userList = ArrayList<String>()
        val userListData = ArrayList<Data>()

        val client = ApiClient.getInstance()
        val responseCall = client.getAllUsers()

        responseCall.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful && response.body()?.data != null) {
                    val dataFromApi = response.body()?.data ?: emptyList()

                    userListData.clear()
                    userList.clear()

                    userListData.addAll(dataFromApi)

                    for (i in dataFromApi) {
                        userList.add(i.employeeName ?: "Tanpa Nama")
                    }

                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        userList
                    )
                    binding.lvNama.adapter = listAdapter
                } else {
                    Toast.makeText(this@MainActivity, "Gagal memuat data (HTTP ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        binding.lvNama.setOnItemClickListener { _, _, position, _ ->
            if (position < userListData.size) {
                val selectedUser = userListData[position]
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra("EXTRA_ID", selectedUser.id ?: -1)
                startActivity(intent)
            }
        }
    }
}