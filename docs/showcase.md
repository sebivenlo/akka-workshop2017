## LOGwear Knowledge-base backend application

The Backend of the LOGwear Knowledge-base is responsible for CRUD operation and feeding json to different applications based on a fairly complex Mongo database:

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/logweardomain.png "Animation of the algorithm")


In order to get wearables associated with a process one needs to retrieve the foreign id of ActivityInProcess, the ids of the activities in ActivityInProcess, the DataAssociation from the Activities, etc.

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/domain.gif "Animation of the algorithm")
