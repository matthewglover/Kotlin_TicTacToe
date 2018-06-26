package ui

class MockIO : IO {
  val toRead: MutableList<String> = mutableListOf()
  val written: MutableList<String> = mutableListOf()
  private val funCalls: MutableList<String> = mutableListOf()

  override fun clearScreen() {
    funCalls.add("clearScreen")
  }

  override fun read(): String? {
    funCalls.add("read")
    return toRead.removeAt(0)
  }

  override fun write(line: String) {
    funCalls.add("write")
    written.add(line)
  }

  fun callsTo(funName: String) =
      funCalls
          .filter { it == funName }
          .count()
}