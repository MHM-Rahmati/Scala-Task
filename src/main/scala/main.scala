import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import utils.*

@main def main: Unit =
  /*########################
     Task-1 : Read json files
  ########################*/
  val clicksData = jreader("clicks.json", "clicks")
  val impressionsData = jreader("impressions.json", "impressions")

  /*########################
     Convert to list of map's
  ########################*/
  val listOfClicksMap = new ListBuffer[Map[String, Any]]()
  clicksData.foreach(elem => for it <- elem do listOfClicksMap += getCCParams(it))
  val listOfImpressionsMap = new ListBuffer[Map[String, Any]]()
  impressionsData.foreach(elem => for it <- elem do listOfImpressionsMap+=getCCParams(it))

  /*########################
       Task-2 : Dimensions and Statistics
  ########################*/
  val cm = scala.collection.mutable.Map[String, List[Double]]() withDefaultValue (List(0.0, 0.0))
  for (e <- listOfClicksMap; k = e("impression_id").toString) cm.update(k, List(cm(k).head + e("revenue").asInstanceOf[Double], cm(k)(1) + 1))

  //clicksMap is : (id, sum(revenue), count(clicks)
  val clicksMap = cm.map((k, v) => Map("id"->k, "revenue"->v(0), "clicks"->v(1))).toList

  //impressionsMap is : (id, app_id, country_code, count(impressions))
  val impressionsMap = listOfImpressionsMap
    .groupBy(m => (m("app_id"), m("country_code"), m("id")))
    .view.mapValues(_.size).toMap
    .map((k,v) => Map("app_id"->k(0), "country_code"->k(1), "id"->k(2), "impressions"->v))

  // Merge (impressions, clicks)
  val mergedMaps = clicksMap++impressionsMap
  // Group maps by their "id" attribute
  val groupedMaps = mergedMaps.groupBy(_("id"))
  // Concatenate the maps in each group into a single map
  val concatenatedMaps = groupedMaps.map { case (id, maps) => maps.reduce(_ ++ _).updated("id", id)}
  // Add missing attributes to each map in the list
  val allAttributesMaps = concatenatedMaps.map { map =>
    val allKeys = mergedMaps.flatMap(_.keys).distinct
    allKeys.foldLeft(map)((acc, key) => acc.updated(key, acc.getOrElse(key, None)))
  }
  //Calculate final result of Task-2
  val Task2Result = allAttributesMaps
    .groupBy(e => (e("app_id").toString, e("country_code").toString))
    .mapValues { lm =>
      val impressionsSum = lm.map(_("impressions").asInstanceOf[Int]).sum
      val clicksSum = lm.map(em => if em("clicks")==None then 0.0 else em("clicks").asInstanceOf[Double]).sum
      val revenueSum = lm.map(em => if em("revenue")==None then 0.0 else em("revenue").asInstanceOf[Double]).sum
      (impressionsSum, clicksSum, revenueSum)
    }
    .view.toMap.map(EM => Map("app_id"->EM(0)(0), "country_code"->EM(0)(1),
    "impressions"->EM(1)(0), "clicks"->EM(1)(1), "revenue"->EM(1)(2)))
  //Write result to file
  saveListAsJson(Task2Result, "Task2_output.json")

  /*########################
       Task-3 : Top 5 advertisers
  ########################*/
  val impressionsMapAd = listOfImpressionsMap
    .groupBy(m => (m("app_id"), m("country_code"), m("id"), m("advertiser_id")))
    .view.mapValues(_.size).toMap
    .map((k, v) => Map("app_id" -> k(0), "country_code" -> k(1), "id" -> k(2), "advertiser_id" -> k(3), "impressions" -> v))

  // merge and group by like Task-2
  val mergedMapsAd = clicksMap ++ impressionsMapAd
  val groupedMapsAd = mergedMapsAd.groupBy(_("id"))
  val concatenatedMapsAd = groupedMapsAd.map { case (id, maps) => maps.reduce(_ ++ _).updated("id", id) }
  val allAttributesMapsAd: Iterable[Map[String, Any]] = concatenatedMapsAd.map { map =>
    val allKeys = mergedMapsAd.flatMap(_.keys).distinct
    allKeys.foldLeft(map)((acc, key) => acc.updated(key, acc.getOrElse(key, None)))
  }
  //Calculate final result of Task-3
  val Task3Result = allAttributesMapsAd.map(e => Map("app_id" -> e.getOrElse("app_id", 0),
    "country_code" -> e.getOrElse("country_code", 0),
    "advertiser_id" -> e.getOrElse("advertiser_id", 0),
    if e("revenue")==None then
      "rate" -> 0
    else if e("impressions")!=0 then
      "rate" -> e("revenue").toString.toDouble/e("impressions").toString.toDouble
    else
      "rate" -> 0))
    .toList.groupBy(m => (m("app_id"), m("country_code")))
    .flatMap { case ((app_id, country_code), ms) => ms.sortBy(_("rate").toString.toDouble)
    .reverse.take(5)}
    .groupBy(m => (m("app_id"), m("country_code")))
    .flatMap{ case ((k, v), aglist) =>
      List(aglist.map(m => m("advertiser_id")))
        .map(m => Map("app_id"->k, "country_code"->v, "recomended_advertiser_ids"->m))}
    .collect{
      case map => map.view.mapValues(_.asInstanceOf[Matchable]).toMap
    }
  //Write result to file
  saveListAsJson(Task3Result, "Task3_output.json")