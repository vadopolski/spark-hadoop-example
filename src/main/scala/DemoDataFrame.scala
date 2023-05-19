import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object DemoDataFrame extends App {

  import ReadWriteUtils._

  implicit val spark = SparkSession
    .builder()
    .appName("Introduction to RDDs")
    .config("spark.master", "local")
    .getOrCreate()

  def processTaxiData(taxiFactsDF: DataFrame, taxiZoneDF: DataFrame): DataFrame =
    taxiFactsDF
      .join(broadcast(taxiZoneDF), col("DOLocationID") === col("LocationID"), "left")
      .groupBy(col("Borough"))
      .agg(
        count("*").as("total trips"),
        round(min("trip_distance"), 2).as("min distance"),
        round(mean("trip_distance"), 2).as("mean distance"),
        round(max("trip_distance"), 2).as("max distance")
      )
      .orderBy(col("total trips").desc)


  val taxiFactsDF: DataFrame = readParquet("src/main/resources/yellow_taxi_jan_25_2018")

  taxiFactsDF.printSchema()

  val l: String = taxiFactsDF.schema.toDDL
  println(l)

  taxiFactsDF
    .coalesce(1)
    .write
    .mode(SaveMode.Overwrite)
    .json("src/main/resources/json/yellow_taxi_jan_25_2018")


  val taxiZoneDF: DataFrame = readCSV("src/main/resources/taxi_zones.csv")

  val result: DataFrame = processTaxiData(taxiFactsDF, taxiZoneDF)

  result.show()

}