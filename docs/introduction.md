# Introduction
The actor model is a computational model like the touring machine that is designed around concurrency.


# The thread model
<img align="top" src="https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/threadModel.svg">
In the thread model, threads share memory in order to work together. Values shared between threads nee d to be locked in order to assure their integrity. With locks come deadlocks, race condition and a difficult implementation process.


# The actor model
<img align="top" src="https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/actorModel.svg">
The actor model avoids locking issues by specifically disallowing shared memory between actors. Each actor has private memory that is invisible to the rest of the sytem. Values that need to be shared can only be exchanged through messages.

# Actor = Independed machine

<img align="top" src="https://github.com/sebivenlo/akka-workshop2017/blob/master/resources/system-1527685_640.png">
Because of the independed memory we can think of an actor as an independed machine in a computer network. When solving a problem with the actor model, the core question is not *How do I solve this with multiple threads?* but *How do solve this with multiple computers?*. Because of this principle it is quite easy to create a destributed computing network. Think cloud platform and docker!

# Communication
The actor models core idea is to enable concurrecy through communication. It makes no assumptions on the nature of the actors and where they are located. They can run on remote and local Windows, Linuxm, OS X, JVM..... BAASD ALDASLD KASDKASPD KASPD KAS   Because of this the model does not include message reliability. 
