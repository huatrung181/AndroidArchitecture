package com.example.filterphoto.kotlin

import java.io.Serializable
import kotlin.properties.Delegates

class FilterData(name:String, rule:String, imageId:Int) : Serializable{

    lateinit var name: String
    lateinit var rule: String
    var imageId by Delegates.notNull<Int>()


}