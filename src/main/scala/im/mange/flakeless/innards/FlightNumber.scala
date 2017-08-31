package im.mange.flakeless.innards

/**
  * Created by pall on 31/08/2017.
  */
//TODO: in Config, have option to forget previous flight data when calling newFlight
//TODO: store summary on reset for later ...
object FlightNumber {
  private val currentFlightNumberCounter = AtomicIntCounter()

  def next = currentFlightNumberCounter.next
}
