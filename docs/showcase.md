# LOGwear Knowledge-base backend application

The Backend of the LOGwear Knowledge-base is responsible for CRUD operation and feeding json to different applications based on a fairly complex Mongo database.

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/logweardomain.png "LOGwear domain model")


In order to get wearables associated with a process one needs to retrieve the foreign id of ActivityInProcess, the ids of the activities in ActivityInProcess, the DataAssociation from the Activities, etc.

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/domain.gif "Animation of the algorithm")

# Domain Definition and Path finding
The Domain Model is defined in code and can be adjusted if need be. This is used to generate the database and defines a path between the types.

```java

        DOMAIN.add(
          new TypeDescription(PROCESSCATEGORIES).
                addRelation(PROCESSES, MANY)
        );

        DOMAIN.add(
          new TypeDescription(PROCESSES).
            addRelation(PROCESSCATEGORIES, MANY).
            addRelation(ACTIVITYINPROCESS, ONE)
          );

        DOMAIN.add(
          new TypeDescription(ACTIVITYINPROCESS, true).
            addRelation(PROCESSES, ONE).addRelation(ACTIVITIES, MANY)
        );
        
       DOMAIN.add(
        new TypeDescription(DATAOBJECTCLASSES).
          addRelation(DATAOBJECTS, MANY).
          addRelation(WEARABLEFACILITATIONS, MANY).
          addRelation(DATAOBJECTCLASSES, MANY));
       );
        ...
        
 ```

# Required input to extract data from the database
In order to extract a Process and its associated Wearables, one only needs to specify the Process with an id and the last type in the path (in this case Wearables). A simple pathfinding algorithm finds the complete path __(Process, ActivityInProcess, Activity, DataAssociation, DataObject, DataObjectClasses, WearableFacilitation, Interfaces, Wearables)__ required to extract the desired data.


## Akka
With the starting entity and path defined, AKKA comes into play. 
Concurrent extraction of entities can begin as soon as an entity has more than one connection to the next domain type (e.g the connections from AIP to Activities). 

