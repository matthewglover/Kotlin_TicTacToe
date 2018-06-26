package ui

interface IO {
  fun clearScreen()
  fun read(): String?
  fun write(t: String)
}