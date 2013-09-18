Triana Cloud
============

The TrianaCloud software infrastructure was developed during work on the SHIWA project, to provide a way to distribute executable tasks across a distributed architecture.

Specifically, the tasks being run were instances of the Triana Workflow Engine. Triana has been modified to accept and output SHIWA Bundles, which describe a sub-section of a workflow, or an entire workflow. Additionally, a SHIWA Bundle includes the metadata necessary to allow the execution a workflow, 

A SHIWA Bundle encapsulates the input,data, the binaries and dependencies required for execution, and the resulting outputs. With this mechanism, it became possible to distribute discrete data packages between distributed computation nodes, resulting in a much more dynamic and scalable execution environment.

The TrianaCloud infrastructure was designed as a method of providing communication channels between Cloud nodes, in a manner which was completely agnostic to the actual execution required by the workflow. As TrianaCloud is provided Open Source, this has led to other uses of TrianaCloud by other research projects, in ways not originally expected.

Technical
---------

The main backbone of TrianaCloud is provided by RabbitMQ, a Java implementation of the AMQP protocol. Data packets are passed between nodes using these messaging channels, and execution status can be polled and monitored via HTTP requests.

Usages
------

TrianaCloud was used to distribute workflows in the SHIWA Project, while investigating the potential for passing data between different workflow environments using the IWIR workflow toolkit.

Additionally, workflows were distributed while researching an implementation for the STAMPEDE logging framework, comparing the utility of a common logging system compatible with both the Triana and Pegasus workflow engines.

More recently, an evolution of TrianaCloud has been utilised in the TARDIS project to distribute sentiment mining tools across a distributed Cloud architecture, processing data retrieved from the Twitter API.



Installation
------------

RabbitMQ must be installed somewhere, so that it will be available to both the Broker and the Workers.

On RabbitMQ, create a topic exchange, marking it as durable.
Create a queue marked durable, with auto ack disabled, linking it to the exchange you just created,
and set the routing key to something relevant (e.g. dart.triana, or *.triana).

Create queues for each routing key you'll be using.

Doing it this way means that if there are no workers running, tasks are still held in the queues,
rather than there being no queue, or queues being deleted.


## Documentation

Javadocs - http://cscsi.github.io/TrianaCloud/


[![Build Status](http://build.dbyz.co.uk/buildStatus/icon?job=TrianaCloud)](http://build.dbyz.co.uk/job/TrianaCloud/)
