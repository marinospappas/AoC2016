package mpdev.springboot.aoc2016.utils.aocvm

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import mpdev.springboot.aoc2016.utils.aocvm.ProgramState.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractAocVm(instructionList: List<String>, private val instanceNamePrefix: String) {

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    // the AoCVM "process table"
    private val instanceTable = mutableListOf<AocInstance>()

    init {
        // clears the instance table and creates the first instance of the AocCode program
        instanceTable.clear()
        setupNewInstance(instructionList)
    }

    companion object {
        const val DEF_PROG_INSTANCE_PREFIX = "aocprog"
    }

    protected fun setupNewInstance(instructionList: List<String>): Int {
        val ioChannels = mutableListOf<Channel<Long>>(Channel(UNLIMITED),Channel(UNLIMITED))
        instanceTable.add(AocInstance(Program(instructionList, ioChannels), ioChannels))
        val programId = instanceTable.lastIndex
        instanceTable[programId].program.instanceName = "$instanceNamePrefix-$programId"
        log.info("AocCode instance [$programId] configured")
        return programId
    }

    protected fun aocCtl(programId: Int, cmd: AocCmd, value: Any) {
        when (cmd) {
            AocCmd.SET_OUTPUT_BUFFER_SIZE -> instanceTable[programId].ioChannels[1] = Channel(value as Int)
        }
    }

    /// protected / internal functions
    suspend fun runAocProgram(programId: Int, initReg: Map<String, Long> = emptyMap()) {
        instanceTable[programId].program.run(initReg)
    }

    protected fun aocProgramIsRunning(programId: Int) =
        instanceTable[programId].program.programState != COMPLETED

    protected suspend fun waitAocProgram(job: Job) {
        job.join()
    }

    protected suspend fun setProgramInput(data: List<Long>, programId: Int) {
        log.debug("set program input to {}", data)
        setInputValues(data, instanceTable[programId].ioChannels[0])
    }

    protected suspend fun getProgramFinalOutputLong(programId: Int): List<Long> {
        log.debug("getProgramFinalOutputLong called")
        delay(1)      // required in case the program job is still waiting for input
        while (instanceTable[programId].program.programState == RUNNING) {     // job active = still producing output
            delay(1)
        }
        val output = getOutputValues(instanceTable[programId].ioChannels[1])
        log.debug("returning output: {}", output)
        return output
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected suspend fun getProgramAsyncOutputLong(programId: Int): List<Long> {
        log.debug("getProgramAsyncOutputLong called")
        val outputValues = mutableListOf<Long>()
        outputValues.add(instanceTable[programId].ioChannels[1].receive())
        while (!instanceTable[programId].ioChannels[1].isEmpty) {
            outputValues.add(instanceTable[programId].ioChannels[1].receive())
        }
        log.debug("returning output: {}", outputValues)
        return outputValues
    }

    private suspend fun setInputValues(values: List<Long>, inputChannel: Channel<Long>) {
        values.forEach { v -> inputChannel.send(v) }
    }

    private suspend fun getOutputValues(outputChannel: Channel<Long>): List<Long> {
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
        instanceTable[programId].program.setMemory(address, data)
    }

    protected fun setProgramMemory(programId: Int, address: Int, data: Int) {
        setProgramMemory(programId, address, data.toLong())
    }

    protected fun getProgramMemoryLong(programId: Int, address: Int) = instanceTable[programId].program.getMemory(address)

    protected fun getProgramMemory(programId: Int, address: Int) = getProgramMemoryLong(programId, address).toInt()

    protected fun getProgramRegisterLong(programId: Int, reg: String) = instanceTable[programId].program.getRegister(reg)

    protected fun getProgramRegister(programId: Int, reg: String) = getProgramRegisterLong(programId, reg).toInt()

    class AocInstance(val program: Program, val ioChannels: MutableList<Channel<Long>>)

    enum class AocCmd {
        SET_OUTPUT_BUFFER_SIZE
    }
}