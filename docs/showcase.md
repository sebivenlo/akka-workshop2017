# LOGwear Knowledge-base backend application

The Backend of the LOGwear Knowledge-base is responsible for CRUD operation and feeding json to different applications based on a fairly complex Mongo database.

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/logweardomain.png "LOGwear domain model")


In order to get wearables associated with a process one needs to retrieve the foreign id of ActivityInProcess, the ids of the activities in ActivityInProcess, the DataAssociation from the Activities, etc.

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/domain.gif "Animation of the algorithm")


# Required input for the database operation
In order to extract a Process and its associated Wearables, one only needs to specify the Process with an id and the last type in the path (in this case Wearables). A simple pathfinding algorithm finds the complete path __(Process, ActivityInProcess, Activity, DataAssociation, DataObject, DataObjectClasses, WearableFacilitation, Interfaces, Wearables)__ required to extract the desired data. 


## Akka
With the starting entity and path defined, the database can be queried. Concurrent extraction of entities can begin as soon as an entity has more than one connection to the next domain type (e.g the connections from AIP to Activities). 
Basicly, each time the backend received a request it creates an _AbstractControllingActor_ which creates a large number of __DocumentActors__. The implementation of _AbstractControllingActor_ defines the operation to be executed and defines how the responses from the __DocumentActors__ are processed. 

![alt text](https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/sequence.svg "Sequence diagram showing the interaction between actors")

1. The ___AbstractControllingActor___ creates a new __DocumentActor__ and sends it a __QueryMessage__ which defines the ObjectId and the collections of the Process to be extracted.

2. Upon receiving the __QueryMessage__, the __DocumentActor__ extracts the Process from __MongoDB__.

3. The __DocumentActor__ invokes the __execute__ method of the __DocumentOperation__ implementation provided in the __QueryMessage__. In this case the database document is transformed to a specific JSON standart. 

4. The __DocumentOperation__ is executed. It sends its result to the ___AbstractControllingActor___.

5. The implementation of ___handleActorResponse___ handles the Result of the __DocumentOperation___.

6. The __DocumentActor__ sends a __QuertyMessage__ for each connected entity of the next type (It extracts the ActivityInProcess id from the Process document and creates a __QueryMessage__ with that id and ACTIVITYINPROCESS as type. The rest of the data is copied from the initial __QueryMessage__. 

7. The ___AbstractControllingActor___ checks whether the entity specified in the received __QueryMessage__ qm has already been processed. If not, it creates a new __DocumentActor__ and forwards qm to it. (This is the start of recursion)
