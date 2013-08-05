Triana Cloud
============

Description of Triana Cloud

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
