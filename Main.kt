import kotlin.math.abs

enum class File(val index: Int) {
    a(0),
    b(1),
    c(2),
    d(3),
    e(4),
    f(5),
    g(6),
    h(7),
}

enum class CAPTURE_TYPE {
    NORMAL, EN_PASSANT
}

const val SIDE_COUNT = 8
const val P2_INIT_RANK = 7
const val P1_INIT_RANK = 2

class Chess {
    private val player1: String
    private val player2: String
    private val square = mutableListOf<MutableList<Char>>()
    private var player1Turn = true
    private var prevTurn = ""

    constructor() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        player1 = readln()
        println("Second Player's name:")
        player2 = readln()
    }

    fun setupBoard() {
        repeat(SIDE_COUNT) {
            when (it) {
                rankToIndex(P2_INIT_RANK) -> square.add(MutableList(8) { 'B' })
                rankToIndex(P1_INIT_RANK) -> square.add(MutableList(8) { 'W' })
                else -> square.add(MutableList(8) { ' ' })
            }
        }
    }

    private fun rankToIndex(rank: Int): Int {
        return abs(rank - SIDE_COUNT)
    }

    fun printBoard() {
        var board = "  +---+---+---+---+---+---+---+---+\n"
        repeat(SIDE_COUNT) {
            board += "${rankToIndex(it)} | ${square[it].joinToString(" | ")} |\n" +
                    "  +---+---+---+---+---+---+---+---+\n"
        }
        board += "    a   b   c   d   e   f   g   h\n"
        println(board)
    }

    private fun canMoveForward(initRank: Int, moveRank: Int, movePosition: Char): Boolean {
        val isFirstMove = initRank == if (player1Turn) P1_INIT_RANK else P2_INIT_RANK
        val isMoveForward =
            (isFirstMove && (((if (player1Turn) -1 else 1) * (initRank - moveRank)) in 1..2)) //first move - 1 or 2 move
                    || (((if (player1Turn) -1 else 1) * (initRank - moveRank)) == 1) // else 1 move
        return isMoveForward && movePosition == ' '
    }

    private fun captureType(initRank: Int, moveRank: Int, moveFile: Char): MutableList<CAPTURE_TYPE> {
        val captureList = mutableListOf<CAPTURE_TYPE>()
        val isMoveForwardDiagonal = ((if (player1Turn) -1 else 1) * (initRank - moveRank)) == 1
        if (!isMoveForwardDiagonal) return captureList

        val haveEnemyPiece =
            square[rankToIndex(moveRank.toString().toInt())][File.valueOf(moveFile.toString()).ordinal] ==
                    if (player1Turn) 'B' else 'W'
        if (haveEnemyPiece) captureList.add(CAPTURE_TYPE.NORMAL)

        if ((square[rankToIndex(initRank.toString().toInt())][File.valueOf(moveFile.toString()).ordinal] ==
                    if (player1Turn) 'B' else 'W') && prevTurn == moveFile.toString() + initRank.toString()
        ) captureList.add(CAPTURE_TYPE.EN_PASSANT)

        return captureList
    }

    private fun movePiece(initRank: Char, initFile: Char, moveRank: Char, moveFile: Char) {
        square[rankToIndex(initRank.toString().toInt())][File.valueOf(initFile.toString()).ordinal] =
            ' '
        square[rankToIndex(moveRank.toString().toInt())][File.valueOf(moveFile.toString()).ordinal] =
            if (player1Turn) 'W' else 'B'
        prevTurn = moveFile.toString() + moveRank.toString()
        player1Turn = !player1Turn
    }

    fun startGame() {
        while (true) {
            println("${if (player1Turn) player1 else player2}'s turn:")
            val input = readln()
            if (input == "exit") {
                println("Bye!")
                break
            }

            if (Regex("[a-h][1-8][a-h][1-8]").matches(input)) {
                val initFile = input[0]
                val moveFile = input[2]
                val initRank = input[1]
                val moveRank = input[3]
                val initPosition =
                    square[rankToIndex(initRank.toString().toInt())][File.valueOf(initFile.toString()).ordinal]
                val movePosition =
                    square[rankToIndex(moveRank.toString().toInt())][File.valueOf(moveFile.toString()).ordinal]
                if (isSquareHavePlayerPiece(initPosition)) {
                    val isSameFile = initFile == moveFile
                    if (isSameFile && canMoveForward(
                            initRank.toString().toInt(),
                            moveRank.toString().toInt(),
                            movePosition
                        )
                    ) {
                        movePiece(initRank, initFile, moveRank, moveFile)
                        printBoard()
                        continue
                    }

                    val isMovingToSide =
                        abs(File.valueOf(initFile.toString()).ordinal - File.valueOf(moveFile.toString()).ordinal) == 1
                    val captureTypes = captureType(initRank.toString().toInt(), moveRank.toString().toInt(), moveFile)
                    if (isMovingToSide && captureTypes.size > 0) {
                        movePiece(initRank, initFile, moveRank, moveFile)
                        for (captureType in captureTypes) {
                            when (captureType) {
                                CAPTURE_TYPE.EN_PASSANT -> square[rankToIndex(
                                    initRank.toString().toInt()
                                )][File.valueOf(moveFile.toString()).ordinal] =
                                    ' '

                                else -> {}
                            }
                        }
                        printBoard()
                        continue
                    }

                } else {
                    println("No ${if (player1Turn) "white" else "black"} pawn at ${input.substring(0, 2)}")
                    continue
                }
            }
            println("Invalid Input")
        }
    }

    private fun isSquareHavePlayerPiece(initPosition: Char): Boolean {
        return initPosition == if (player1Turn) 'W' else 'B'
    }
}

fun main() {
    val chess = Chess()
    chess.setupBoard()
    chess.printBoard()
    chess.startGame()
}