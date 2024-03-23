package mpdev.springboot.aoc2016.config

import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PuzzleSolversConfig {

    @Autowired
    lateinit var puzzleSolversList: List<PuzzleSolver>

    @Bean
    fun puzzleSolvers(): Map<Int,PuzzleSolver>  = puzzleSolversList.associateBy { solver -> solver.day }
}