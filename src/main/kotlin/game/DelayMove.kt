package game

import java.util.concurrent.TimeUnit

class DelayMove(private val interval: Long) {
  fun run() { TimeUnit.MILLISECONDS.sleep(interval) }
}
