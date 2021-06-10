package id.ac.unhas.listapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.barang.view.*
import kotlinx.android.synthetic.main.content_main.*
import org.w3c.dom.Text
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private var listBarang = ArrayList<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            var intent = Intent(this, BarangActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listBarang.clear()
        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val jumlah = cursor.getString(cursor.getColumnIndex("Jumlah"))
                val harga = cursor.getString(cursor.getColumnIndex("Harga"))

                listBarang.add(Barang(id, nama, jumlah, harga))
            } while (cursor.moveToNext())
        }
        var barangAdapter = BarangAdapter(this, listBarang)
        lvBarang.adapter = barangAdapter
    }

    inner class BarangAdapter: BaseAdapter{

        private var barangList = ArrayList<Barang>()
        private var context: Context? = null

        constructor(context: Context, barangList: ArrayList<Barang>) : super() {
            this.barangList = barangList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.barang, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mBarang = barangList[position]

            vh.tvNama.text = mBarang.name
            vh.tvJumlah.text = mBarang.jumlah
            vh.tvHarga.text = "Rp." + mBarang.harga

            lvBarang.onItemClickListener = AdapterView.OnItemClickListener {adapterView, viwe, position, id ->
                updateBarang(mBarang)
            }
            return view
        }

        override fun getItem(position: Int): Any {
            return barangList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return barangList.size
        }

    }

    private fun updateBarang(barang: Barang) {
        var intent = Intent (this, BarangActivity::class.java)
        intent.putExtra("MainActId", barang.id)
        intent.putExtra("MainActNama", barang.name)
        intent.putExtra("MainActJumlah", barang.jumlah)
        intent.putExtra("MainActHarga", barang.harga)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvNama: TextView
        val tvJumlah: TextView
        val tvHarga: TextView

        init {
            this.tvNama = view?.findViewById(R.id.tvNama) as TextView
            this.tvJumlah = view?.findViewById(R.id.tvJumlah) as TextView
            this.tvHarga = view?.findViewById(R.id.tvHarga) as TextView
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}