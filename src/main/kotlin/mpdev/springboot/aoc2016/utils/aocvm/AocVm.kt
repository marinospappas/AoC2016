package mpdev.springboot.aoc2016.utils.aocvm

import kotlinx.coroutines.Job

open class AocVm(aocProgram: List<String>,
                 instanceNamePrefix: String = DEF_PROG_INSTANCE_PREFIX
): AbstractAocVm() {

    private var mainInstance: Program

    init {
        // clears the instance table and creates the first instance of the AocCode program
        if (instanceTable.isNotEmpty())
            instanceTable.clear()
        instanceTable.add(Program(aocProgram, listOf(ioChannels[0].first, ioChannels[0].second)))
        mainInstance = instanceTable[0]
        mainInstance.instanceName = "$instanceNamePrefix-0"
        log.info("AocCode instance [0] configured")
    }

    suspend fun runProgram(initReg: Map<String, Long> = emptyMap()) {
        log.info("AocCode instance [0] starting")
        runAocProgram(0, initReg)
    }

    suspend fun sendInputToProgram(data: Int) {
        setAocProgramInputLong(listOf(data.toLong()), 0)
    }

    suspend fun sendInputToProgram(data: List<Int>) {
        setAocProgramInputLong(data.map { it.toLong() }, 0)
    }

    suspend fun getOutputFromProgram() = getAocProgramOutputLong(0).map { it.toInt() }

    suspend fun getOutputFromProgramLong() = getAocProgramOutputLong(0)

    fun programIsRunning() = aocProgramIsRunning(0)

    suspend fun waitProgram(job: Job) {
        waitAocProgram(job)
        log.info("AocProg instance [0] completed")
    }

    fun setProgramMemory(address: Int, data: Long) {
        instanceTable[0].setMemory(address, data)
    }

    fun setProgramMemory(address: Int, data: Int) {
        setProgramMemory(0, address, data.toLong())
    }

    fun getProgramMemoryLong(address: Int) = getProgramMemoryLong(0, address)

    fun getProgramMemory(address: Int) = getProgramMemoryLong(address).toInt()

    fun getProgramRegisterLong(reg: String) = getProgramRegisterLong(0, reg)

    fun getProgramRegister(reg: String) = getProgramRegisterLong(reg).toInt()

    fun setProgramRegister(reg: String, data: Long) {
        setProgramRegisterLong(0, reg, data)
    }

    fun setProgramRegister(reg: String, data: Int) {
        setProgramRegister(reg, data.toLong())
    }
}