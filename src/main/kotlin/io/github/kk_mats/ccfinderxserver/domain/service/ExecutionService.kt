package io.github.kk_mats.ccfinderxserver.domain.service

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import io.github.kk_mats.ccfinderxserver.domain.repository.DetectorInfoRepository
import io.github.kk_mats.ccfinderxserver.domain.type.*
import io.github.kk_mats.ccfinderxserver.domain.type.query.DetectionQuery
import io.github.kk_mats.ccfinderxserver.domain.type.response.DetectionResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths

@Service
class ExecutionService {
    @Autowired
    lateinit var detectorInfoRepository: DetectorInfoRepository

    val queryWriter: ObjectWriter = ObjectMapper().writer(DefaultPrettyPrinter())

    fun run(query: DetectionQuery): Failable<DetectionResponse> {
        val output = Files.createTempDirectory(Paths.get(query.output), null);

        val ll = Label("-o")
        val value = query.parameters[ll]
        if (value != null) {
            query.parameters[ll] = Paths.get("c:/outputs", output.fileName.toString(), value).toString()
        }

        query.parameters[Label("-dn")] = Paths.get("c:/projects", query.target.relative).toString()

        val options = query.parameters.toList()
                .sortedWith(Comparator { l1, l2 -> l2.first.priority.compareTo(l1.first.priority) })
                .flatMap { (label, value) -> listOf(label.option, value) }
                .toTypedArray()

        val pb = ProcessBuilder("docker", "exec", "ccfx-c", "powershell", "${query.version.option}", *options).apply {
            redirectErrorStream(true)
        }

        val ins = Capture(pb)

        if (ins.exitCode != 0) {
            return fail(FailureCode.detectionFailed("java ${options.joinToString(" ")}", ins.input.toString()))
        }

        this.queryWriter.writeValue(output.resolve("query.json").toFile(), query);

        return succeed(DetectionResponse(output.fileName.toString(), output.toString(), listOf(), ins.input.toString()))
    }
}

class Capture(processBuilder: ProcessBuilder) {
    private var running = true
    val input = StringBuilder()
    val exitCode: Int

    init {
        val process = processBuilder.start()
        this.loadInputStream(process)
        this.exitCode = process.waitFor()
        this.running = false
    }

    private fun loadInputStream(p: Process) {
        val reader = BufferedReader(InputStreamReader(p.inputStream))
        Thread {
            reader.use { reader ->
                var line: String?
                while (this.running) {
                    line = reader.readLine()
                    if (line != null) {
                        this.input.append(line + "\n")
                    }
                }
            }
        }.start()
    }
}