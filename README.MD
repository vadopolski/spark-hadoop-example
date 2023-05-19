/usr/bin/spark-submit \
--class TestAppMain \
--master yarn \
--deploy-mode client \
--verbose \
--supervise \
amr-spark-application.jar s3://test-123123123123/movies/movies.json s3://test-123123123123/goodMovies



/usr/bin/spark-submit \
--class TestAppMain \
--master yarn \
--deploy-mode client \
--verbose \
--supervise \
spark-hadoop-example.jar s3://test-123123123123/movies/movies.json s3://test-123123123123/goodMovies
