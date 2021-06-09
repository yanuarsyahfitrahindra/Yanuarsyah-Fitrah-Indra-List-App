package id.ac.unhas.listapp

class Barang {
    var id: Int? = null
    var name: String? = null
    var jumlah: String? = null
    var harga: String? = null

    constructor(id: Int, name: String, jumlah: String, harga: String){
        this.id = id
        this.name = name
        this.jumlah = jumlah
        this.harga = harga
    }
}