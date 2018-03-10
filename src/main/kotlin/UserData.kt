package server

import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Polygon
import org.bson.codecs.pojo.annotations.BsonId

data class Login (val mail: String,val pass: String)

data class User(@BsonId val mail:String, val nome: String, val pass: String)

data class Place(@BsonId val coordinates: Point, val name: String, val description: String, val area: Polygon)

//{coordinates=[20,20] name="Il gusto" description="suca" area=[[10,10],[20,10],[20,20],[10,20],[10,10]]