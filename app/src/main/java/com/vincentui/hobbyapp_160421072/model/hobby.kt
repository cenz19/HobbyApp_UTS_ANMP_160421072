package com.vincentui.hobbyapp_160421072.model

data class Hobby(
    val id:Int?,
    val maker:String?,
    val title:String?,
    val shortdesc:String?,
    val img:String?,
    val details:HobbyDetail?
)
data class HobbyDetail(
    val paragraph:List<String>?,
)
