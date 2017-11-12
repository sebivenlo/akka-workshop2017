# Introduction
The actor model is a computational model that consists of actors instead of threads.


# The thread model
<img align="top" src="https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/threadModel.svg">
In the thread model, threads share memory in order to work together. Values shared between threads need to be locked in order to assure their integrity. With locks come deadlocks, race condition and a difficult implementation process.


# The actor model
<img align="top" src="https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/actorModel.svg">
The actor model avoids locking issues by specifically disallowing shared memory between actors. Each actor has private memory that is invisible to the rest of the sytem. Values that need to be shared can only be exchanged through messages.
