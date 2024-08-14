package mpdev.springboot.aoc2016.utils.aocvm

import kotlinx.coroutines.Job

open class AocVm(instructionList: List<String>, instanceNamePrefix: String = DEF_PROG_INSTANCE_PREFIX):
    AbstractAocVm(instructionList, instanceNamePrefix) {

    fun newProgram(instructionList: List<String>) = setupNewInstance(instructionList)

    fun aocCtl(cmd: AocCmd, value: Any, programId: Int = 0) {
        aocCtl(programId, cmd, value)
    }

    suspend fun runProgram(initReg: Map<String, Long> = emptyMap(), programId: Int = 0) {
        log.info("AocCode instance [$programId] starting")
        runAocProgram(programId, initReg)
    }

    suspend fun sendInputToProgram(data: Int, programId: Int = 0) {
        setProgramInput(listOf(data.toLong()), programId)
    }

    suspend fun sendInputToProgram(data: List<Int>, programId: Int = 0) {
        setProgramInput(data.map { it.toLong() }, programId)
    }

    suspend fun getFinalOutputFromProgram() = getProgramFinalOutputLong(0).map { it.toInt() }
    suspend fun getAsyncOutputFromProgram(programId: Int = 0) = getProgramAsyncOutputLong(programId).map { it.toInt() }

    suspend fun getFinalOutputFromProgramLong() = getProgramFinalOutputLong(0)
    suspend fun getAsyncOutputFromProgramLong(programId: Int = 0) = getProgramAsyncOutputLong(programId)

    fun programIsRunning() = aocProgramIsRunning(0)

    suspend fun waitProgram(job: Job) {
        waitAocProgram(job)
        log.info("AocProg instance [0] completed")
    }

    fun setProgramMemory(address: Int, data: Long) {
        setProgramMemory(0, address, data)
    }

    fun setProgramMemory(address: Int, data: Int) {
        setProgramMemory(address, data.toLong())
    }

    fun getProgramMemoryLong(address: Int) = getProgramMemoryLong(0, address)

    fun getProgramMemory(address: Int) = getProgramMemoryLong(address).toInt()

    fun getProgramRegisterLong(reg: String) = getProgramRegisterLong(0, reg)

    fun getProgramRegister(reg: String) = getProgramRegisterLong(reg).toInt()

}