package im.mange.flakeless

case class Config(waitPollPeriod: Int = 1, waitTimeout: Int = 5000
                  , outputDirectory: String = "target/test-reports"
                  , host: Option[String] = Some("http://localhost:63342/root"))
