package im.mange.flakeless.innards

/**
  * Created by pall on 25/05/2017.
  */
case class AtomicIntCounter(start: Int = 1) {
  private var count = start - 1

  def next = synchronized {
    count += 1
    count
  }

  def value = count
}
