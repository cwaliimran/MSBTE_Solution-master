package com.msbte.modelanswerpaper

class model {
    lateinit var filename: String
    lateinit var fileurl: String

    constructor() {}
    constructor(filename: String, fileurl: String, nod: Int, nol: Int, nov: Int) {
        this.filename = filename
        this.fileurl = fileurl
    }
}