package hackman.trevor.copycat.system

/**
 * Turns an int into its corresponding 'excel column' form.
 * That is 1, 2, 3... into A, B, C, ... Z, AA, AB, AC, ... AZ, BA, BB, ... ZZ, AAA, ...
 */
fun intToExcelName(integer: Int): String {
    if (integer < 1) {
        report("Invalid argument, integer can't be less than 1")
        return "ERROR"
    }
    val result = StringBuilder()
    var recursion = integer
    while (recursion > 0) {
        var modulo = recursion % 26
        if (modulo == 0) {
            modulo = 26
        }
        result.insert(0, '@' + modulo) // '@' is character before 'A'
        recursion = (recursion - 1) / 26
    }
    return result.toString()
}
