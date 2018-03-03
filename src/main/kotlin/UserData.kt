package server

import org.bson.codecs.pojo.annotations.BsonId

data class Login (val mail: String,val pass: String)

data class User(@BsonId val mail:String, val nome: String, val pass: String)