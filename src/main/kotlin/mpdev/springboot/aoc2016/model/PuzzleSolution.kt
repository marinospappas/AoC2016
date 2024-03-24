package mpdev.springboot.aoc2016.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PuzzleSolution(
    @JsonProperty("Message")  var message: String = "Advent of Code 2016",
    @JsonProperty("Day")  var day: Int,
    @JsonProperty("initialisation Time (milli-sec)")  var initTime: Long = 0,
    @JsonProperty("Solution")  var solution: Set<PuzzlePartSolution>
)

data class PuzzlePartSolution(
    @JsonProperty("Part") var part: Int = 0,
    @JsonProperty("Result") var result: String = "",
    @JsonProperty("Elapsed Time (milli-sec)") var elapsedTime: Long = 0,
)
