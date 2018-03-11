/*package server

import com.mongodb.client.model.geojson.*
*/

package server

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Polygon

class Login {
    @SerializedName("mail")
    @Expose
    var mail: String? = null

    @SerializedName("pass")
    @Expose
    var pass: String? = null

    constructor() {}

    constructor(mail: String, pass: String) : super()
    {
        this.mail = mail
        this.pass = pass
    }
}

class Place //(val coordinates: Point, val name: String, val description: String, val area: Polygon)
{
    @SerializedName("coordinates")
    @Expose
    var coordinates: Point? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("area")
    @Expose
    var area: Polygon? = null

    constructor() {}

    constructor(coordinates: Point, name: String, description: String, area: Polygon)
    {
        this.coordinates = coordinates
        this.name = name
        this.description = description
        this.area = area
    }
}

class Position //(val coordinates: Point)
{
    @SerializedName("coordinates")
    @Expose
    var coordinates: Point? = null

    constructor(){}

    constructor(coordinates: Point)
    {
        this.coordinates = coordinates
    }
}
//{"newPlace":{"coordinates":{"coordinate":{"values":[15.0,15.0]}},"name":"Il gusto","description":"suca","area":{"coordinates":{"exterior":[{"values":[10.0,10.0]},{"values":[10.0,20.0]},{"values":[20.0,20.0]},{"values":[20.0,10.0]},{"values":[10.0,10.0]}],"holes":[]}}}}


class User {

    @SerializedName("mail")
    @Expose
    var mail: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("pass")
    @Expose
    var pass: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param mail
     * @param name
     * @param pass
     */
    constructor(mail: String, name: String, pass: String) : super() {
        this.mail = mail
        this.name = name
        this.pass = pass
    }

}

