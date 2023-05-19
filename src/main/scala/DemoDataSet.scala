import org.apache.spark.sql.functions.{broadcast, col}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object DemoDataSet extends App {

  import ReadWriteUtils._
  import spark.implicits._
  import model._
  val spark = SparkSession
    .builder()
    .appName("Introduction to RDDs")
    .config("spark.master", "local[2]")
    .getOrCreate()

  val taxiFactsDF: DataFrame =
    spark.read
      .load("src/main/resources/yellow_taxi_jan_25_2018")

  val taxiFactsDS: Dataset[TaxiRide] =
    taxiFactsDF
      .as[TaxiRide]

  val taxiZoneDS: Dataset[TaxiZone] = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("src/main/resources/taxi_zones.csv")
    .as[TaxiZone]

  taxiFactsDS
    .filter(x => x.DOLocationID != 0)
    .join(broadcast(taxiZoneDS), col("DOLocationID") === col("LocationID"), "left")
    .groupBy(col("Borough"))
    .count()
    .orderBy(col("count").desc)
    .show()

}
