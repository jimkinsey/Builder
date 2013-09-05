Builder
=======

An experiment in using the Scala reflection API and Dynamic to produce a generic builder.

    case class Person(firstName: String, lastName:String, age:Int)

    ...

    val oldJohnSmith = new Builder[Person].withFirstName("John").andLastName("Smith").andAge(112).build
    assert(oldJohnSmith == Person("John", "Smith", 112)

It provides sensible basic defaults when a field has not been specified:

    val youngCharley = new Builder[Person].withFirstName("Charley").build
    assert(manWithNoName == Person("Charley", "", 0)

It is immutable, allowing the "fixing" of fields:

    val jonesBuilder = new Builder[Person].withLastName("Jones")
    val cyranoJones = jonesBuilder.withFirstName("Cyrano").build
    val christmasJones = jonesBuilder.withFirstName("Christmas").build

Why?
----

* I wanted to play around with the Scala reflection API
* Also, the Dynamic trait
* I've seen a lot of boiler-plate builder classes in my time

Notes
-----

Currently only supports case classes. Will work with some other classes but behaviour is not guaranteed.

Where it can't work out a default value it uses null. I don't like this either. I may well change it.
