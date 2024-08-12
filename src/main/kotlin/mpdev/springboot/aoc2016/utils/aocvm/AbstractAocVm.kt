package mpdev.springboot.aoc2016.utils.aocvm

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import mpdev.springboot.aoc2016.utils.aocvm.ProgramState.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractAocVm {

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        const val INSTANCE_TABLE_SIZE = 50
        const val DEF_PROG_INSTANCE_PREFIX = "aocprog"
        // the AoCVM "process table"
        val instanceTable = mutableListOf<Program>()
        // the AoC input/output channels
        val ioChannels = Array<Pair<Channel<Long>, Channel<Long>>>(INSTANCE_TABLE_SIZE)
        { Pair(Channel(Channel.UNLIMITED), Channel(10)) }
    }

    /// protected / internal functions
    suspend fun runAocProgram(programId: Int = 0, initReg: Map<String, Long> = emptyMap()) {
        instanceTable[programId].run(initReg)
    }

    protected fun aocProgramIsRunning(programId: Int = 0) =
        instanceTable[programId].programState != COMPLETED

    protected suspend fun waitAocProgram(job: Job) {
        job.join()
    }

    protected suspend fun setProgramInput(data: List<Long>, programId: Int = 0) {
        log.debug("set program input to {}", data)
        setInputValues(data, ioChannels[programId].first)
    }

    protected suspend fun getProgramFinalOutputLong(programId: Int = 0): List<Long> {
        log.debug("getProgramFinalOutputLong called")
        delay(1)      // required in case the program job is still waiting for input
        while (instanceTable[programId].programState == RUNNING) {     // job active = still producing output
            delay(1)
        }
        val output = getOutputValues(ioChannels[programId].second)
        log.debug("returning output: {}", output)
        return output
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected suspend fun getProgramAsyncOutputLong(programId: Int = 0): List<Long> {
        log.debug("getProgramAsyncOutputLong called")
        val outputValues = mutableListOf<Long>()
        outputValues.add(ioChannels[programId].second.receive())
        while (!ioChannels[programId].second.isEmpty) {
            outputValues.add(ioChannels[programId].second.receive())
        }
        log.debug("returning output: {}", outputValues)
        return outputValues
    }

    private suspend fun setInputValues(values: List<Long>, inputChannel: Channel<Long> = ioChannels[0].first) {
        values.forEach { v -> inputChannel.send(v) }
    }

    private suspend fun getOutputValues(outputChannel: Channel<Long> = ioChannels[0].second): List<Long> {
        val outputValues = mutableListOf<Long>()
        outputValues.add(outputChannel.receive())
        do {
            val nextItem = outputChannel.tryReceive().getOrNull()
            if (nextItem != null)
                outputValues.add(nextItem)
        } while(nextItem != null)
        return outputValues
    }

    protected fun setProgramMemory(programId: Int, address: Int, data: Long) {
        instanceTable[programId].setMemory(address, data)
    }

    protected fun setProgramMemory(programId: Int, address: Int, data: Int) {
        setProgramMemory(programId, address, data.toLong())
    }

    protected fun getProgramMemoryLong(programId: Int, address: Int) = instanceTable[programId].getMemory(address)

    protected fun getProgramMemory(programId: Int, address: Int) = getProgramMemoryLong(programId, address).toInt()

    protected fun getProgramRegisterLong(programId: Int, reg: String) = instanceTable[programId].getRegister(reg)

    protected fun getProgramRegister(programId: Int, reg: String) = getProgramRegisterLong(programId, reg).toInt()

    protected fun setProgramRegisterLong(programId: Int, reg: String, data: Long) {
        instanceTable[programId].setRegister(reg, data)
    }

    protected fun setProgramRegister(programId: Int, reg: String, data: Int) {
        setProgramRegisterLong(programId, reg, data.toLong())
    }
}