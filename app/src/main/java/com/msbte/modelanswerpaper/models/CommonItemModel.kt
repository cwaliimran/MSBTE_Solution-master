package com.msbte.modelanswerpaper.models

class CommonItemModel {
    var name = ""
    var path = ""
    var sub_domain = ""

    constructor() {}
    constructor(name: String, path: String, sub_domain: String) {
        this.name = name
        this.path = path
        this.sub_domain = sub_domain
    }

    fun getsubDomain(): String {
        return sub_domain
    }

    fun setsubDomain(subDomain: String) {
        sub_domain = subDomain
    }
}