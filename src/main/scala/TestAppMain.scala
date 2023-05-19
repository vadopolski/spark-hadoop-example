import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}

object TestAppMain {

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("Need input path and output path")
      System.exit(1)
    }

    val spark = SparkSession
      .builder()
      .appName("Vopolski Application")
      .getOrCreate()

    val moviesDF = spark.read
      .option("inferSchema", "true")
      .json(args(0))

    val goodComediesDF = moviesDF.select(
      col("Title"),
      col("IMDB_Rating").as("Rating"),
      col("Release_Date").as("Release")
    )
      .where(col("Major_Genre") === "Comedy" and col("IMDB_Rating") > 6.5)

      // shuffle 200

      .orderBy(col("Rating").desc_nulls_last)

    goodComediesDF.show

    goodComediesDF.write
      .mode(SaveMode.Overwrite)
      .format("json")
      .save(args(1))
  }

}
