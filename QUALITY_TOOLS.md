# Herramientas de Calidad de Código

Este proyecto utiliza tres herramientas complementarias para garantizar la calidad del código:

## 🛠️ Herramientas Configuradas

### 1. Android Lint
**Propósito:** Detecta problemas específicos de Android (APIs, recursos, seguridad)
- ✅ Integrado por defecto con Android Gradle Plugin
- 📊 Genera reportes en HTML, XML y SARIF

### 2. ktlint
**Propósito:** Formateo consistente de código Kotlin
- ✅ Aplica la guía oficial de estilo de Kotlin
- 🔧 Puede auto-formatear automáticamente

### 3. Detekt
**Propósito:** Análisis estático profundo (complejidad, code smells, arquitectura)
- ✅ Detecta problemas de complejidad ciclomática
- ✅ Identifica anti-patterns y code smells
- ✅ Valida patrones arquitectónicos (MVI, Clean Architecture)

---

## 📋 Comandos Disponibles

### Formateo de Código

```bash
# Auto-formatear todo el código con ktlint
./gradlew formatCode

# Solo verificar formato (sin modificar archivos)
./gradlew ktlintCheck
```

### Análisis de Calidad

```bash
# Ejecutar TODAS las herramientas (ktlint + detekt + lint)
./gradlew qualityCheck

# Ejecutar solo Detekt
./gradlew detekt

# Ejecutar solo Android Lint
./gradlew lint
```

### Verificación Rápida

```bash
# Check rápido antes de commit (solo ktlint)
./gradlew quickCheck

# Check completo pre-commit (ktlint + detekt)
./gradlew preCommit
```

---

## 🚀 Flujo de Trabajo Recomendado

### Durante el Desarrollo

1. **Antes de empezar a codear:**
   ```bash
   ./gradlew formatCode
   ```

2. **Mientras programas:**
   - El plugin de ktlint en Android Studio formateará automáticamente
   - Guarda frecuentemente para ver warnings en tiempo real

3. **Antes de hacer commit:**
   ```bash
   ./gradlew preCommit
   ```
   Si falla, corrige los problemas y vuelve a intentar.

### Adopción Incremental

Si tienes código existente con muchos warnings:

```bash
# Generar baseline de Detekt (congela issues actuales)
./gradlew detektBaseline

# Generar baseline de Android Lint
./gradlew lintDebug --continue
```

Los baselines permiten adoptar estas herramientas sin bloquear desarrollo.
Solo se reportarán **nuevos** problemas introducidos después del baseline.

---

## 📊 Ubicación de Reportes

Después de ejecutar los checks, los reportes se generan en:

### ktlint
- No genera reportes (solo output en consola)
- Errores se muestran directamente en terminal

### Detekt
```
build/reports/detekt/
├── detekt.html     ← Reporte visual (abrir en navegador)
├── detekt.xml      ← Para integración CI/CD
├── detekt.txt      ← Formato texto plano
└── detekt.sarif    ← Para GitHub Security
```

### Android Lint
```
app/build/reports/
├── lint-results-debug.html   ← Reporte visual
├── lint-results-debug.xml    ← Para integración CI/CD
└── lint-results-debug.sarif  ← Para GitHub Security
```

---

## 🔧 Configuración

### ktlint
- Configuración en: `build.gradle.kts` (root)
- Versión: 1.6.0
- Basado en: Guía oficial de estilo de Kotlin

### Detekt
- Configuración en: `config/detekt/detekt.yml`
- Baseline: `config/detekt/baseline.xml`
- Reglas optimizadas para MVI + Clean Architecture

### Android Lint
- Configuración en: `app/build.gradle.kts` y módulos Android
- Baselines: `lint-baseline.xml` en cada módulo
- Reglas críticas habilitadas: Security, Performance, Compose

---

## ❗ Solución de Problemas

### "Build failed: ktlint check failed"

**Solución:**
```bash
# Auto-formatear para corregir problemas
./gradlew ktlintFormat

# Si persiste, revisa el output para ver qué no se puede auto-arreglar
./gradlew ktlintCheck
```

### "Build failed: Detekt found issues"

**Opción 1 - Corregir los problemas:**
- Abre `build/reports/detekt/detekt.html` en navegador
- Revisa los issues y corrígelos manualmente

**Opción 2 - Congelar issues existentes (temporal):**
```bash
./gradlew detektBaseline
```
Esto creará/actualizará el baseline, congelando los problemas actuales.

### "Build failed: Lint found errors"

**Opción 1 - Corregir los errores:**
- Abre `app/build/reports/lint-results-debug.html`
- Corrige los errores críticos reportados

**Opción 2 - Generar baseline (temporal):**
```bash
./gradlew lintDebug --continue
```
El archivo `lint-baseline.xml` se actualizará automáticamente.

---

## 🎯 Métricas de Calidad

### Objetivos del Proyecto

| Métrica | Target | Estado Actual |
|---------|--------|---------------|
| Warnings Detekt | < 50 | Por medir |
| Warnings Lint | < 20 | Por medir |
| Violaciones ktlint | 0 | ✅ Logrado |
| Complejidad ciclomática | < 15 | Monitoreando |

---

## 🔗 Referencias

- [ktlint GitHub](https://github.com/pinterest/ktlint)
- [Detekt Documentation](https://detekt.dev/)
- [Android Lint Checks](https://googlesamples.github.io/android-custom-lint-rules/checks/index.html)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)

---

## 💡 Tips

### Ignorar warnings específicos (usar con precaución)

**Detekt:**
```kotlin
@Suppress("ComplexMethod", "LongMethod")
fun complexLegacyFunction() {
    // código complicado heredado
}
```

**Android Lint:**
```kotlin
@SuppressLint("HardcodedText")
@Composable
fun DebugOnlyComponent() {
    Text("DEBUG MODE") // Solo para desarrollo
}
```

**ktlint:**
```kotlin
// ktlint-disable max-line-length
val veryLongUrlThatCannotBeSplit = "https://api.example.com/v1/very/long/endpoint/that/makes/sense/to/keep/in/one/line"
// ktlint-enable max-line-length
```

⚠️ **Importante:** Solo suprime warnings cuando tengas una razón válida y documéntala con un comentario.

---

## 📚 Para Más Información

Consulta con el equipo de arquitectura o revisa:
- Configuración de Detekt: `config/detekt/detekt.yml`
- Configuración de Lint: `app/build.gradle.kts` (sección `lint {}`)
- Configuración de ktlint: `build.gradle.kts` (root, sección `subprojects {}`)