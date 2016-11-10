<a href="https://travis-ci.org/alltonp/flakeless" target="_blank"><img src="https://travis-ci.org/alltonp/flakeless.png?branch=master"></a>

Usage
-----
Add the following lines to your build.sbt

    resolvers += "Sonatype Repo" at "http://oss.sonatype.org/content/groups/public/"

    libraryDependencies += "im.mange" %% "flakeless" % "version"


Code license
------------
Apache License 2.0


TODO:
- support element path or SafeWebElement
- In() -> WebElement
- change order ... Click(By.foo, In(webDriver))
               ... AssertElementTextEquals(By.foo, expected, In(element))

- in config, use i statements to setup ..
               iAcceptThatFindingByAnythingButIdMayIntroduceFlakeyness
               iAcceptThatNotCheckingForASingleUniqueElementMayIntroduceFlakeyness
               iAcceptThatFindingFromWebElementsMayIntroduceFlakeyness

assetCount0is another smell ... aka empty

make the affirmations be opt in ...
    so migrate upwards from flaky to flakeless

- e.g. .disallowZeroElementCounts