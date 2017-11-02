<a href="https://travis-ci.org/alltonp/flakeless" target="_blank"><img src="https://travis-ci.org/alltonp/flakeless.png?branch=master"></a>


# Flakeless - light touch, flake free browser testing in scala


### Why would I use it?
- You want to write your tests in blissful ignorance of async/ajax browser updates
- You never want to write another sleep, explicit, implicit or fluent wait
- You value zero tolerance to flaky tests

### How?
- No big bang switch, flakeless lets you gradually migrate your tests one interaction at a time
- Replace each action or assertion with Flakeless' simple [Tell Dont Ask](https://martinfowler.com/bliki/TellDontAsk.html) style primitives


### Sounds good, how do I start eradicating my existing flaky tests?
- Read the [Migration Guide](src/example/scala/im/mange/flakeless/examples/MigrationGuide.scala)



### Installing

Add the following lines to your build.sbt (click on the 'build passing' link above to get the version number)

    resolvers += "Sonatype Repo" at "http://oss.sonatype.org/content/groups/public/"

    libraryDependencies += "im.mange" %% "flakeless" % "version"


-----

Copyright © 2016-2017 Spabloshi Ltd
