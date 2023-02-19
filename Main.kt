package chess

fun main() {
//    write your code here
    println("Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayer = readln()
    println("Second Player's name:")
    val secondPlayer = readln()
    println("""  +---+---+---+---+---+---+---+---+
8 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
7 | B | B | B | B | B | B | B | B |
  +---+---+---+---+---+---+---+---+
6 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
5 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
4 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
3 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
2 | W | W | W | W | W | W | W | W |
  +---+---+---+---+---+---+---+---+
1 |   |   |   |   |   |   |   |   |
  +---+---+---+---+---+---+---+---+
    a   b   c   d   e   f   g   h  """)
    var firstPlayerTurn = true
    while (true) {
        if (firstPlayerTurn)
            println("$firstPlayer's turn:")
        else
            println("$secondPlayer's turn:")
        val input = readln()
        if (input.lowercase() == "exit"){
            println("Bye!")
            break
        }
        if (Regex("[a-h][1-8][a-h][1-8]").matches(input))
            firstPlayerTurn = !firstPlayerTurn
        else
            println("Invalid Input")
    }
}