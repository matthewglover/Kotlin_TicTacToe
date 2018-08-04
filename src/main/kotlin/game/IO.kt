package game

interface IO {
  fun clearScreen()
  fun readLine(): String?
  fun write(text: String)
}
