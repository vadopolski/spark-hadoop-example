package inside

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{col, expr}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter


object InsideApp extends App {
  // creating a SparkSession
  val spark = SparkSession.builder()
    .appName("DataFrames Basics")
    .config("spark.master", "local")
//    .config("spark.sql.codegen.comments", "true")
    .getOrCreate()

  import spark.implicits._

  val valueDF =
    spark.sparkContext
      .parallelize(Seq(("k1", 123), ("k2", 234), ("k3", 345), ("k4", 567), ("k5", 567), ("k5", 567)))
      .toDF("first", "second")

  val resultDF = valueDF
    .where($"first" =!= "k1")
    .withColumn("new", expr("second + 1"))
    .sort($"second".desc, $"first")

  valueDF.show()
  valueDF.queryExecution.debug.codegen
}
