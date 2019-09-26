# Flink CEP combined with PA model

This project was a part of my Bachelor Thesis written at DIMA Research Group, Technical University of Berlin, summer term 2019.

## Requrements
You will need to install and run with default settings local instances of: Apache Kafka, InfluxDB and Grafana. 

You can then import the Grafana dashboard from `grafana-dashboard.json`

Please also check the properties in `software.anton.pcep.configs.Configuration`

## How to run
You have to run the _main()_ method of following classes in the exact order:
1. `PredictionAnnotationJob.java`
2. `ConsumerJob.java`
3. `ProducerJob.java`
