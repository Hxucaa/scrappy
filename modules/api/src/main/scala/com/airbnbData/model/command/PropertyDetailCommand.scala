package com.airbnbData.model.command

import java.net.URL

import io.circe.Json

/**
  * Created by Lance on 2016-11-22.
  */

case class PropertyDetailCommand(
                             id: Long = 0L,
                             //                     belongsTo: Array[AirbnbUser],
                             bathrooms: Int,
                             bedrooms: Int,
                             beds: Int,
                             city: String,
                             name: String,
                             personCapacity: Int,
                             propertyType: String,
                             publicAddress: String,
                             roomType: String,
                             document: Json,
                             summary: String,
                             address: String,
                             description: String,
                             airbnbUrl: URL
                           ) extends Command

object PropertyDetailCommand {

  import io.circe._
  import io.circe.optics.JsonPath._


  def fromJson(json: Json): Option[PropertyDetailCommand] = {
    val base = root.listing

    //    val geometryFactory = new com.vividsolutions.jts.geom.GeometryFactory(new com.vividsolutions.jts.geom.PrecisionModel())
    // FIXME: Try a cleaner implementation with decoder instead of optics.
    for {
      propertyType <- base.property_type.string.getOption(json)
      publicAddress <- base.public_address.string.getOption(json)
      roomType <- base.room_type.string.getOption(json)
      document <- base.json.getOption(json)
      summary <- base.summary.string.getOption(json)
      address <- base.address.string.getOption(json)
      description <- base.description.string.getOption(json)
      id <- base.id.long.getOption(json)
      airbnbUrl <- Some(new java.net.URL("https://api.airbnb.com/v2/listings/" + id.toString))
      bathrooms <- base.bathrooms.int.getOption(json) match {
        case None => base.bathrooms.double.getOption(json).map(_.toInt)
        case Some(v) => Some(v)
      }
      bedrooms <- base.bedrooms.int.getOption(json)
      beds <- base.beds.int.getOption(json)
      city <- base.city.string.getOption(json)
      //                    geopoint <- for {
      //                      lat <- base.lat.double.getOption(json)
      //                      lng <- base.lng.double.getOption(json)
      //                    } yield geometryFactory.createPoint(new com.vividsolutions.jts.geom.Coordinate(lng, lat))
      name <- base.name.string.getOption(json)
      personCapacity <- base.person_capacity.int.getOption(json)
    } yield PropertyDetailCommand(
      id,
      bathrooms,
      bedrooms,
      beds,
      city,
      name,
      personCapacity,
      propertyType,
      publicAddress,
      roomType,
      document,
      summary,
      address,
      description,
      airbnbUrl
    )
  }
}