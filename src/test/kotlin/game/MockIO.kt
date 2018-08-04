package game

class MockIO : IO {
  val toRead: MutableList<String> = mutableListOf()
  val written: MutableList<String> = mutableListOf()
  private val funCalls: MutableList<String> = mutableListOf()

  override fun clearScreen() {
    funCalls.add("clearScreen")
  }

  override fun readLine(): String? {
    funCalls.add("readLine")
    return toRead.removeAt(0)
  }

  override fun write(text: String) {
    funCalls.add("write")
    written.add(text)
  }

  fun callsTo(funName: String) =
      funCalls
          .filter { it == funName }
          .count()
}
