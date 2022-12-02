const val sourceKey = 1003
const val sourceMessage = 200

val sTableZeroth = mutableListOf(
    mutableListOf(1, 0, 3, 2),
    mutableListOf(3, 2, 1, 0),
    mutableListOf(0, 2, 3, 1),
    mutableListOf(3, 1, 3, 2)
)

val sTableFirst = mutableListOf(
    mutableListOf(0, 1, 3, 2),
    mutableListOf(2, 0, 1, 3),
    mutableListOf(3, 0, 3, 0),
    mutableListOf(2, 1, 3, 0)
)

val pFour = listOf(2, 4, 3, 1)
val pTen = listOf(3, 5, 2, 7, 4, 10, 1, 9, 8, 6)
val pEight = listOf(6, 3, 7, 4, 8, 5, 10, 9)
val IP = listOf(2, 6, 3, 1, 4, 8, 5, 7)
val ipINV = listOf(4, 1, 3, 5, 7, 2, 8, 6)
val eP = listOf(4, 1, 2, 3, 2, 3, 4 , 1)


fun moveToBinary(initial: Int): String {
    return initial.toString(2)
}


fun moveToLeft(sourceText: String): String {
    val sourceTextFirstLONE = sourceText.substring(0, sourceText.length / 2)
    val sourceTextSecondLONE = sourceText.substring(sourceText.length / 2, sourceText.length)

    return "${sourceTextFirstLONE.substring(1, sourceTextFirstLONE.length)
            }${sourceTextFirstLONE.first()}${sourceTextSecondLONE.substring(1, sourceTextSecondLONE.length)
            }${sourceTextSecondLONE.first()}"
}


fun convertTo(sourceText: String, sList: List<Int>): String {
    var result = ""
    val sourceTextList = Array(sourceText.length) { i -> sourceText[i] }
    for (el in sList) result += sourceTextList[el - 1]

    return result
}


fun xor(firstKey: String, stringEP: String): String {
    var result = ""
    for (i in firstKey.indices){
            result += if (firstKey[i] != stringEP[i]) "1"
            else "0"
    }

    return result
}


fun convertToSTable(sourceText: String, sList: MutableList<MutableList<Int>>): String {
    val row = (sourceText.first() + sourceText.substring(sourceText.length - 2, sourceText.length - 1)).toInt(2)
    val col = sourceText.substring(1, sourceText.length - 1).toInt(2)
    var result =  sList[row][col].toString(2)

    if (result == "1") result = "0$result"
    else if (result == "0") result = "0$result"
    return result
}


fun function(ep: List<Int>, s0: MutableList<MutableList<Int>>, s1: MutableList<MutableList<Int>>, key: String, message: String): String {
    val messageLeft = message.substring(0, 4)
    val messageRight = message.substring(4, message.length)
    val convertToEP = convertTo(messageRight, ep)
    val firstXor = xor(convertToEP, key)
    val sTableLeft = convertToSTable(firstXor.substring(0, 4), s0)
    val sTableRight = convertToSTable(firstXor.substring(4, firstXor.length), s1)
    val convertToP4 = convertTo(sTableLeft + sTableRight, pFour)

    return xor(messageLeft, convertToP4) + messageRight
}


fun main() {
    /** Key Generation */
    val firstKey = convertTo(moveToLeft(convertTo(moveToBinary(sourceKey), pTen)), pEight)
    val secondKey = convertTo(moveToLeft(moveToLeft(moveToLeft(convertTo(moveToBinary(sourceKey), pTen)))), pEight)

    /** Encryption */
    val stepToIpENC = convertTo(moveToBinary(sourceMessage), IP)
    val fKFirstENC = function(eP, sTableZeroth, sTableFirst, firstKey, stepToIpENC)
    val swapENC = fKFirstENC.substring(4, fKFirstENC.length) + fKFirstENC.substring(0, 4)
    val fKSecondENC = function(eP, sTableZeroth, sTableFirst, secondKey, swapENC)
    val cipherText = convertTo(fKSecondENC, ipINV)

    /** Decryption */
    val stepToIpDEC = convertTo(cipherText, IP)
    val fKFirstDEC = function(eP, sTableZeroth, sTableFirst, secondKey, stepToIpDEC)
    val swapDEC = fKFirstDEC.substring(4, fKFirstDEC.length) + fKFirstDEC.substring(0, 4)
    val fKSecondDEC = function(eP, sTableZeroth, sTableFirst, firstKey, swapDEC)
    val decryptedText = convertTo(fKSecondDEC, ipINV)

    println("Source Message  ━  ${moveToBinary(sourceMessage)} & $sourceMessage\n")
    println("$firstKey  ━  1 \uD83D\uDD11")
    println("$secondKey  ━  2 \uD83D\uDD11")
    println("\nEncrypted Text  ━  $cipherText")
    println("\nDecrypted Text  ━  $decryptedText & ${decryptedText.toInt(2)}")
}
