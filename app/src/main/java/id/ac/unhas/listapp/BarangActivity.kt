package id.ac.unhas.listapp

import android.content.ContentValues
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_barang.*

class BarangActivity : AppCompatActivity() {

    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang)

        try {
            var bundle: Bundle = intent.extras
            id = bundle.getInt("MainActId", 0)
            if (id !=0){
                txNama.setText(bundle.getString("MainActNama"))
                txJumlah.setText(bundle.getString("MainActJumlah"))
                txHarga.setText(bundle.getString("MainActHarga"))
            }
        } catch (ex: Exception){
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)

        val itemDelete: MenuItem = menu.findItem(R.id.action_delete)

        if(id == 0){
            itemDelete.isVisible = false
        } else {
            itemDelete.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_save -> {
                var dbAdapter = DBAdapter( this)

                var values= ContentValues()
                values.put("Nama", txNama.text.toString())
                values.put("Jumlah", txJumlah.text.toString())
                values.put("Harga", txHarga.text.toString())

                if (id == 0) {
                    val mID = dbAdapter.insert(values)

                    if (mID > 0) {
                        Toast.makeText(this, "Tersimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal untuk menyimpan data", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    var selectionArgs = arrayOf(id.toString())
                    val mID = dbAdapter.update(values, "Id=?", selectionArgs)
                    if (mID > 0) {
                        Toast.makeText(this, "Diperbaharui", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal untuk memperbaharui", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            R.id.action_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus Data")
                builder.setMessage("Data ini akan terhapus")

                builder.setPositiveButton("Delete") {dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DBAdapter(this)
                    val selectionArgs = arrayOf(id.toString())
                    dbAdapter.delete("id=?", selectionArgs)
                    Toast.makeText(this, "Terhapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Batal") {dialog: DialogInterface?, which: Int -> }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
        }
    }
}