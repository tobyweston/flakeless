package im.mange.flakeless

case class Config(waitPollPeriod: Int = 1, waitTimeout: Int = 5000
                  , reportDirectory: String = "target/test-reports"
                  , reportHost: Option[String] = Some("http://localhost:63342/root"))
