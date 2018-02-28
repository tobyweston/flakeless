package im.mange.flakeless

case class Config(waitPollPeriod: Int = 1, waitTimeout: Int = 5000
                  , sendKeysDelayMillis: Option[Int] = None
                  , reportDirectory: String = "target/flakeless"
                  , reportHost: Option[String] = Some("http://localhost:63342/root")
                 )
