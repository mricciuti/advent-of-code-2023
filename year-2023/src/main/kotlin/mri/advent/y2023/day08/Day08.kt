package mri.advent.y2023.day08

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.maths.leastCommonMultiple
import java.util.function.Predicate


// data model ----------------------------------------------

enum class Direction(val code: Char) { LEFT('L'), RIGHT('R') }

data class Node(val name: String, val left: String, val right: String) {
    fun next(direction: Direction) = if (direction == Direction.LEFT) left else right
    override fun toString() = "$name: $left;$right"
}

// parsing : AAA = (BBB, BBB)
fun String.toNode(): Node = with(this.split(" = ")) {
    val (left, right) = this[1].split(", ").map { it.trim().replace("(", "").replace(")", "") }
    Node(this[0].trim(), left, right)
}

// create infiny sequence of Directions to navigate in graph.
class InstructionSequence(private val instructions: List<Direction>, private var currentIndex: Int = 0) {
    fun next(): Direction {
        if (currentIndex >= instructions.size) currentIndex = 0
        return instructions[currentIndex++]
    }
}

data class Graph(val directions: List<Direction>, val nodes: Map<String, Node>) {

    /**
     * Returns the number of steps to reach a target node from a given starting node.
     * @param start Starting node in graph
     * @param targetReached Predicate to detect if target node is reached.
     */
    fun nbStepsToReachTarget(start: Node, targetReached: Predicate<Node>): Long {
        val instructions = InstructionSequence(directions)
        var current = start
        var nbSteps = 0L
        while (!targetReached.test(current)) {
            current = nodes[current.next(instructions.next())] ?: throw IllegalStateException("Node not found")
            nbSteps++
        }
        return nbSteps
    }
}

fun parseGraph(lines: List<String>) = Graph(
    lines.first().map { c -> Direction.entries.first { c == it.code } }.toList(),
    lines.drop(2).map { it.toNode() }.associateBy { it.name }
)

/** --- Day 08:  --- */
class Day08(inFile: String = "/day08.in") : BaseDay(inFile) {

    override fun part1() = with(parseGraph(data.lines())) {
        nbStepsToReachTarget(start = nodes["AAA"]!!) { node -> node.name == "ZZZ" }
    }

    override fun part2() = with(parseGraph(data.lines())) {
        /*
        tryPart2(this)
        return ""
         */
        
        val stepsFromStartPositions = this.nodes.entries
            .filter { it.key.endsWith('A') }
            .map { nbStepsToReachTarget(start = it.value) { node -> node.name.endsWith('Z') } }

        stepsFromStartPositions.leastCommonMultiple()
    }

}

fun debug(message: Any) {
    println(message)
}

fun main() {
    Day08().run()
    // part 1 : 18157
    // part 2 : 14299763833181,  14 ms
}


fun tryPart2(graph: Graph) {

    val startNodes = graph.nodes.values.filter { it.name.endsWith('A') }
    debug("starting nodes: ${startNodes.joinToString(" , ") { "[${it}]" }}")
    // sample: 2 neouds  [11A: 11B;XXX] , [22A: 22B;XXX]
    // real  : 6 noeuds  [AAA: JXS;MFQ] , [RLA: JSN;JVD] , [QLA: TSH;RRN] , [QFA: QQR;HDH] , [RXA: NLJ;JPG] , [JSA: TNJ;JXC]

    val startNodeTargetIndices = mutableListOf<List<Int>>()
    startNodes.take(2).forEach { start ->
        debug("\nSTarting node $start  : ")
        val instructions = InstructionSequence(graph.directions)
        var current = start
        val path = mutableListOf<Node>()
        repeat(500_000) {
            path.add(current)
            val next = instructions.next()
            //debug(" step $it : ${current}  next:$next  " + if (current.name.endsWith("Z")) " !!!!!!!!!!!!!! " else "")
            current = graph.nodes[current.next(next)]!!
        }
        val targetIndices = path.mapIndexed { index, node -> index to node }.filter { it.second.name.endsWith("Z") }.map { it.first }
        debug(targetIndices.take(20))
        startNodeTargetIndices.add(targetIndices.take(targetIndices.size))
        /*
         Sample:
            STarting node 11A: 11B;XXX  :
            [2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30,
               => cycle A->Z = 2
            STarting node 22A: 22B;XXX  :
            [3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36, 39, 42, 45,
                => cycle A->Z = 3

         Real input :
            STarting node AAA: JXS;MFQ  :
            [18157, 36314, 54471, 72628, 90785, 108942, 127099, 145256, 163413, 181570, 199727, 217884, 236041, 254198, 272355, 290512, 308669, 326826, 344983, 363140]

            STarting node RLA: JSN;JVD  :
            [14363, 28726, 43089, 57452, 71815, 86178, 100541, 114904, 129267, 143630, 157993, 172356, 186719, 201082, 215445, 229808, 244171, 258534, 272897, 287260]

            STarting node QLA: TSH;RRN  :
            [16531, 33062, 49593, 66124, 82655, 99186, 115717, 132248, 148779, 165310, 181841, 198372, 214903, 231434, 247965, 264496, 281027, 297558, 314089, 330620]

            STarting node QFA: QQR;HDH  :
            [12737, 25474, 38211, 50948, 63685, 76422, 89159, 101896, 114633, 127370, 140107, 152844, 165581, 178318, 191055, 203792, 216529, 229266, 242003, 254740]

            STarting node RXA: NLJ;JPG  :
            [19783, 39566, 59349, 79132, 98915, 118698, 138481, 158264, 178047, 197830, 217613, 237396, 257179, 276962, 296745, 316528, 336311, 356094, 375877, 395660]

            STarting node JSA: TNJ;JXC  :
            [19241, 38482, 57723, 76964, 96205, 115446, 134687, 153928, 173169, 192410, 211651, 230892, 250133, 269374, 288615, 307856, 327097, 346338, 365579, 384820]
         */
    }
    // Search common steps
    repeat(startNodeTargetIndices.size) { idx ->
        val candidate = startNodeTargetIndices[idx].firstOrNull { step ->
            startNodeTargetIndices.filterIndexed { index, ints -> index != idx }
                .any { it.contains(step) }
        }
        debug(" found for $idx ? $candidate")
    }

    // step commun entre noeuds
    //    STarting node AAA: JXS;MFQ  :
    //    [18157, 36314, 54471, 72628, 90785, 108942, 127099, 145256, 163413, 181570, 199727, 217884, 236041, 254198, 272355, 290512, 308669, 326826, 344983, 363140]
    //
    //    STarting node RLA: JSN;JVD  :
    //    [14363, 28726, 43089, 57452, 71815, 86178, 100541, 114904, 129267, 143630, 157993, 172356, 186719, 201082, 215445, 229808, 244171, 258534, 272897, 287260]
    val nbCommun = 18157 * 14363
      val ppcm = 962321
    val instructions = InstructionSequence(graph.directions)
    var start1 = startNodes[0]
    var start2 = startNodes[1]
    repeat(ppcm) {step ->
        val nextDir = instructions.next()
        start1 = graph.nodes[start1.next(nextDir)]!!
        start2 = graph.nodes[start2.next(nextDir)]!!
    }
    debug(" end 1 : $start1")
    debug(" end 2 : $start2")
/*
 end 1 : ZZZ: MFQ;JXS
 end 2 : JJZ: JVD;JSN
 */


}
