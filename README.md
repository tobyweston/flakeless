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