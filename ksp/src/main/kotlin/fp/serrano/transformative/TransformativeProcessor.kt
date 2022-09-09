package fp.serrano.transformative

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.ksp.*
import java.util.concurrent.atomic.AtomicBoolean

internal class TransformativeProcessor(
  private val codegen: CodeGenerator,
  private val transform: Boolean,
  private val mutableCopy: Boolean
) : SymbolProcessor {

  private val done = AtomicBoolean(false)

  override fun process(resolver: Resolver): List<KSAnnotated> {
    // only do work on the first round of files
    // this avoids creating new copies for our own files
    if (done.getAndSet(true)) return emptyList()

    resolver
      .getNewFiles()
      .flatMap { it.declarations }
      .filterIsInstance<KSClassDeclaration>()
      .filter { klass ->
        Modifier.DATA in klass.modifiers && klass.primaryConstructor != null
      }
      .forEach(::processClass)

    return emptyList()
  }

  private fun processClass(klass: KSClassDeclaration) {
    if (transform)
      klass.toTransformFunctionKt().writeTo(codeGenerator = codegen, aggregating = false)
    if (mutableCopy)
      klass.toMutableCopyKt().writeTo(codeGenerator = codegen, aggregating = false)
  }
}
