package mri.advent.y2023.day20

import mri.advent.y2023.BaseDay

enum class Pulse { HIGH, LOW }

data class ModuleGraph(val nodes: MutableMap<String, Module> = mutableMapOf()) {
    val buttonModule: ButtonModule = ButtonModule()

    fun init(lines: List<String>) {
        val specs = lines.map { it.toModuleAndOutputs() }
        // create nodes
        specs.forEach { nodes[it.first.name] = it.first }

        // create links
        specs.forEach { spec ->
            val node = nodes[spec.first.name] ?: throw IllegalStateException("Node not found: ${spec.first.name}")
            spec.second.forEach { output ->
                val outputNode = nodes[output] ?: NoType(output)
                if (outputNode is NoType) {
                    debug("added notType module")
                }
                node.outputs.add(outputNode)
                outputNode.inputs.add(node)
            }
        }
        buttonModule.outputs.add(nodes.values.first { it is Broadcaster })
    }

    data class PulseFromTo(val from: Module, val pulse: Pulse, val to: Module) {
        override fun toString() = "${from.name} $pulse ${to.name}"
    }

    data class PulseQueue(val queue: ArrayDeque<PulseFromTo> = ArrayDeque<PulseFromTo>()) {
        val addedPulses = mutableListOf<Pulse>()
        fun add(pulseFromTo: PulseFromTo) {
            addedPulses.add(pulseFromTo.pulse)
            queue.add(pulseFromTo)
        }

        fun removeFirst() = queue.removeFirst()
        fun isNotEmpty() = queue.isNotEmpty()
    }

    fun pressButton(): List<Pulse> {

        val queue = PulseQueue()
        queue.add(PulseFromTo(buttonModule, Pulse.LOW, buttonModule.outputs.first()))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            current.to.reactTo(current.from, current.pulse)?.let { pulse ->
                current.to.outputs.forEach { output -> queue.add(PulseFromTo(current.to, pulse, output)) }
            }
        }
        return queue.addedPulses
    }
}

sealed class Module(val name: String) {
    val inputs = mutableListOf<Module>()
    val outputs = mutableListOf<Module>()

    abstract fun reactTo(from: Module, pulse: Pulse): Pulse?

    override fun toString() = "Module $name (${javaClass.simpleName})"
    override fun equals(other: Any?) = other is Module && other.name.equals(this.name)
    override fun hashCode() = name.hashCode()
}

class FlipFlop(name: String, var state: Boolean = false) : Module(name) {
    override fun reactTo(from: Module, pulse: Pulse): Pulse? {
        if (pulse == Pulse.HIGH) {
            debug("flipFlop $name ignoring signal pulse $pulse")
            return null
        } else {
            state = !state
            val next = if (state) Pulse.HIGH else Pulse.LOW
            debug("flipFlop $name processing signal pulse $pulse, state becomes $state, out pulse is $next")
            return next
        }
    }
}

class Conjunction(name: String, val lastPulseMap: MutableMap<Module, Pulse> = mutableMapOf()) : Module(name) {
    override fun reactTo(from: Module, pulse: Pulse): Pulse? {
        // update momory for that input
        lastPulseMap[from] = pulse
        // high pulse for all input => send low pulse
        if (inputs.all { (lastPulseMap[it] ?: Pulse.LOW) == Pulse.HIGH }) {
            debug("conjunction $name : all state are High => send low")
            return Pulse.LOW
        }
        // else send high pulse
        debug("conjunction $name : not all state are High => send High ")
        return Pulse.HIGH
    }
}

/**
 * When it receives a pulse, it sends the same pulse to all of its destination modules.
 */
class Broadcaster() : Module("broadcaster") {
    override fun reactTo(from: Module, pulse: Pulse) = pulse
}

class ButtonModule() : Module("button") {
    override fun reactTo(from: Module, pulse: Pulse): Pulse? {
        return Pulse.LOW
    }
}

class FoundRxException () : Exception()

class NoType(name: String) : Module(name) {
    override fun reactTo(from: Module, pulse: Pulse): Pulse? {
        if (this.name == "rx") {
            if (pulse == Pulse.LOW) {
                throw FoundRxException()
            }
        }
        return null
    }
}


fun String.toModuleAndOutputs() = with(this.split(" -> ")) {
    val (name, outputs) = this.first() to this.last().split(", ")
    val module = when (name[0]) {
        '%' -> FlipFlop(name.substring(1))
        '&' -> Conjunction(name.substring(1))
        else -> Broadcaster()
    }
    module to outputs
}

/** --- Day 20:  --- */
class Day20(inFile: String = "/day20.in") : BaseDay(inFile) {

    val graph = ModuleGraph().also { it.init(data.lines()) }

    override fun part1(): Any {
        return ""
        val allPulses = mutableListOf<Pulse>()
        repeat(1000) {
            allPulses.addAll(graph.pressButton())
        }
        return allPulses.count { it == Pulse.LOW } * allPulses.count { it == Pulse.HIGH }
    }

    override fun part2(): Any {
        var nbPress = 0
        while(true) {
            try {
                graph.pressButton()
            } catch (ex: FoundRxException){
                return nbPress
            }
            nbPress++
        }
    }

}

fun main() {
    Day20().run()
}

const val DEBUG = false
fun debug(message: Any) {
    if (DEBUG) println(message)
}