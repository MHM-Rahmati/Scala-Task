# Scala-Task
This repository has been created to develop a simple Scala task. This task has been designed to test the ability of Verve_group applicants. You can find the task description in Docs/Task.docx .<br />
<br />
This task is totally about:<br />
  * Read/Write JSON files and type convertion<br />
  * Calculate metrics for some dimensions<br />
  * Work with groupBy, sortBy, map, flatMap, etc for making a recommendation for the top 5 advertiser_ids<br />
<br />
To Develop and test the task, I used :

-Programing language : Scala 3.2.2<br />
-Builder : sbt 1.8.2<br />
-JDK : OracleOpenJDK 1.8.0_144<br />
and IntelliJ IDEA Community Edition<br />

# About project
The first step of any data engineering process like ETL, is to know and dive into data. Here are some of the questions that you should have the answers in your mind.<br />
<br />
1-In case of missed field or wrong type value, do you guarantee data accuracy and do some acts like schema registry? How?<br />
A: In this case, Yes. With using **Scala Case Classes**.<br />
2-Do you handle null values correctly?<br />
A: Yes. It has been handled with Scala **Option/Some**. We could handle it with **".filter(_.XXX.nonEmpty)"**, but it filters some useful information like key=(10,None).<br />
3-is there a unique "id" for each pair of (app_id, country_code, advertiser_id)? Is this true in reverse?<br />
A: Considering the sample data, No. So we should be careful with "join". But sample data says that it's true in reverse.<br />
<br />
In Scala (like some other P.L.) there is an option to choose between normal and **"lazy"** val/methods. You should choose them considering your hardware, etc and it can be useful for improving performance, memory usage or code simplicity.<br />
<br />
Finally, I should say that I have tried to use multiple ways in the same task to examine myself. For example, I used **"circe" library** to read data from JSON files and **"json4s" library** to write JSON data to JSON files.<br />

# Project structure
<pre>
<br />
* You can find file path, etc. in config.
* You can find sample inputs/outputs in the data directory.
|
|--->Docs
|--->src
|     |--->config
|     |--->main
|            |--->scala
|     |--->test
|            |--->scala
|--->data
|     |--->input
|     |--->output
|--->buils.sbt
</pre>
# Future improvement
1. To improve performance, I just tried to do map, groupBy, etc before join. But there are other ways like using laze evaluation that can be useful.
2. It is better to read files with encoding. Because bad files can down the system for a couple of minutes.
3. With spending more time, it's possible to make this program Object-oriented that is more readable, clear, bug tolerant, etc.
# How to run 
1. Change config file variables as you want.
2. Run the main.scala
