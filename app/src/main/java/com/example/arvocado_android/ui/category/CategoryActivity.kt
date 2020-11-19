package com.example.arvocado_android.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.HorizontalItemDecorator
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.adapter.CategoryAdapter
import com.example.arvocado_android.ui.camera.CameraActivity
import com.example.arvocado_android.ui.mypage.MyPageActivity
import com.kravelteam.kravel_android.util.networkErrorToast
import com.kravelteam.kravel_android.util.safeEnqueue
import com.kravelteam.kravel_android.util.startActivity
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import timber.log.Timber

class CategoryActivity : AppCompatActivity() {
    private val categoryAdatper : CategoryAdapter by lazy { CategoryAdapter() }
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initCategory()
        imgCategoryUser.setOnDebounceClickListener {
            startActivity(MyPageActivity::class,false)
        }
    }
    private fun initCategory() {

        rvCategory.apply {
            adapter = categoryAdatper
            addItemDecoration(HorizontalItemDecorator(14))
        }
        networkManager.requestCategory().safeEnqueue(
            onSuccess = {
                if(it.success) {
                    if (!it.data.isNullOrEmpty()) {
                        categoryAdatper.initData(it.data)
                    }
                } else {
                    Timber.e(it.message)
                }
            },
            onFailure = {
            },
            onError = {
                Timber.e(it)
                networkErrorToast()
            }
        )


        categoryAdatper.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: CategoryResponse, pos: Int) {
                Intent(GlobalApp,CameraActivity::class.java).apply {
                    putExtra("c_name",data.c_name)
                    putExtra("c_idx",data.c_idx)
            }.run {
                GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            }
        })
    }
}