package fp.serrano.transformative

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

public class TransformativeProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
    TransformativeProcessor(
      environment.codeGenerator,
      environment.options.parseBoolOrTrue("transform"),
      environment.options.parseBoolOrTrue("mutableCopy")
    )
}

private fun Map<String, String>.parseBoolOrTrue(key: String) =
  this[key]?.toBooleanStrictOrNull() ?: true
