package site.satya.autobahnjavaimpl

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.crossbar.autobahn.wamp.types.ExitInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {
    private lateinit var a: AutoBahnImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            withContext(Dispatchers.IO){
                a = AutoBahnImpl()
                a.connect("wss://staging-webservice.supportgenie.io/ws")
            }
        }
    }
}

