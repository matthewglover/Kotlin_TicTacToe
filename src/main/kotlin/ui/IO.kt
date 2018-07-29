package ui

interface IO {
  fun clearScreen()
  fun readLine(): String?
  fun write(t: String)
}