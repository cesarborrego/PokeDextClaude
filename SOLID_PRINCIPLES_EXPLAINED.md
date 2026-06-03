# Principios SOLID Explicados 📚

## ¿Qué son los Principios SOLID?

SOLID es un acrónimo que representa cinco principios de diseño de software orientado a objetos, propuestos por Robert C. Martin (Uncle Bob). Estos principios ayudan a crear código más **mantenible**, **flexible** y **escalable**.

```
S - Single Responsibility Principle (Principio de Responsabilidad Única)
O - Open/Closed Principle (Principio Abierto/Cerrado)
L - Liskov Substitution Principle (Principio de Sustitución de Liskov)
I - Interface Segregation Principle (Principio de Segregación de Interfaces)
D - Dependency Inversion Principle (Principio de Inversión de Dependencias)
```

---

## 1️⃣ S - Single Responsibility Principle (SRP)

### Definición
> "Una clase debe tener una, y solo una, razón para cambiar"

Una clase debe tener **una sola responsabilidad** o propósito. Si una clase hace demasiadas cosas, es difícil de mantener y probar.

### ❌ Violación del SRP

```kotlin
// MAL - Esta clase tiene múltiples responsabilidades
class User {
    var name: String = ""
    var email: String = ""

    // Responsabilidad 1: Validación
    fun validateEmail(): Boolean {
        return email.contains("@")
    }

    // Responsabilidad 2: Persistencia en base de datos
    fun saveToDatabase() {
        // Código para guardar en DB
    }

    // Responsabilidad 3: Envío de emails
    fun sendWelcomeEmail() {
        // Código para enviar email
    }

    // Responsabilidad 4: Generación de reportes
    fun generateReport(): String {
        return "User Report: $name"
    }
}
```

**Problemas**:
- Si cambia la forma de enviar emails, tenemos que modificar la clase `User`
- Si cambia el esquema de la base de datos, tenemos que modificar la clase `User`
- Difícil de testear: necesitamos mockear DB y servicio de email

### ✅ Aplicando SRP

```kotlin
// BIEN - Cada clase tiene una responsabilidad

// Responsabilidad: Representar datos del usuario
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Responsabilidad: Validar datos
class UserValidator {
    fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun validateName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
}

// Responsabilidad: Persistencia
class UserRepository {
    fun save(user: User) {
        // Código para guardar en DB
    }

    fun findById(id: Int): User? {
        // Código para buscar en DB
    }
}

// Responsabilidad: Notificaciones
class EmailService {
    fun sendWelcomeEmail(user: User) {
        // Código para enviar email
    }
}

// Responsabilidad: Reportes
class UserReportGenerator {
    fun generate(user: User): String {
        return "User Report: ${user.name}"
    }
}
```

**Beneficios**:
- ✅ Cada clase tiene un propósito claro
- ✅ Fácil de testear: cada clase se prueba independientemente
- ✅ Cambios en una responsabilidad no afectan otras
- ✅ Más reutilizable: `EmailService` puede usarse para otros tipos de emails

### 🏗️ SRP en Nuestra Arquitectura PokedexClaude

```kotlin
// ✅ CORRECTO - Separación clara de responsabilidades

// Responsabilidad: Modelo de dominio
data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<PokemonType>
)

// Responsabilidad: Lógica de negocio (obtener lista)
class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int, offset: Int): Result<List<Pokemon>>
}

// Responsabilidad: Acceso a datos
class PokemonRepositoryImpl(
    private val apiService: PokeApiService
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
}

// Responsabilidad: Estado de UI
class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {
    // Solo maneja estado de UI y eventos
}
```

---

## 2️⃣ O - Open/Closed Principle (OCP)

### Definición
> "Las entidades de software deben estar abiertas para extensión, pero cerradas para modificación"

Debes poder **agregar nueva funcionalidad sin modificar código existente**. Se logra mediante abstracción (interfaces, clases abstractas).

### ❌ Violación del OCP

```kotlin
// MAL - Para agregar un nuevo tipo de pago, hay que modificar la clase
class PaymentProcessor {
    fun processPayment(type: String, amount: Double) {
        when (type) {
            "credit_card" -> {
                println("Processing credit card payment: $amount")
                // Lógica de tarjeta de crédito
            }
            "paypal" -> {
                println("Processing PayPal payment: $amount")
                // Lógica de PayPal
            }
            // ❌ Para agregar "bitcoin", tenemos que modificar esta clase
            else -> throw IllegalArgumentException("Unknown payment type")
        }
    }
}
```

**Problema**: Cada vez que queremos agregar un método de pago, modificamos `PaymentProcessor`.

### ✅ Aplicando OCP

```kotlin
// BIEN - Extensible sin modificar código existente

// Abstracción (cerrada para modificación)
interface PaymentProcessor {
    fun processPayment(amount: Double)
}

// Implementaciones (extensión)
class CreditCardProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing credit card payment: $amount")
        // Lógica específica de tarjeta
    }
}

class PayPalProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing PayPal payment: $amount")
        // Lógica específica de PayPal
    }
}

// ✅ Nueva funcionalidad SIN modificar código existente
class BitcoinProcessor : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("Processing Bitcoin payment: $amount")
        // Lógica específica de Bitcoin
    }
}

// Uso polimórfico
class PaymentService(private val processor: PaymentProcessor) {
    fun pay(amount: Double) {
        processor.processPayment(amount)
    }
}
```

**Beneficios**:
- ✅ Agregar nuevo método de pago no requiere modificar `PaymentService`
- ✅ Cada procesador está aislado y es fácil de testear
- ✅ Menos riesgo de romper funcionalidad existente

### 🏗️ OCP en Nuestra Arquitectura

```kotlin
// ✅ CORRECTO - Podemos extender sin modificar

// Abstracción (cerrada para modificación)
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
}

// Implementación con API (extensión)
class ApiPokemonRepository(
    private val apiService: PokeApiService
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        // Implementación con API
    }
}

// ✅ Nueva implementación con Room SIN modificar use cases
class RoomPokemonRepository(
    private val pokemonDao: PokemonDao
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        // Implementación con Room (cache local)
    }
}

// ✅ Implementación híbrida (API + Cache)
class HybridPokemonRepository(
    private val apiRepository: ApiPokemonRepository,
    private val cacheRepository: RoomPokemonRepository
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        // Intenta cache primero, luego API
    }
}

// Los use cases NO cambian, solo inyectamos diferente implementación
class GetPokemonListUseCase(
    private val repository: PokemonRepository  // ✅ Usa abstracción
)
```

---

## 3️⃣ L - Liskov Substitution Principle (LSP)

### Definición
> "Los objetos de una superclase deben poder ser reemplazados por objetos de sus subclases sin romper la aplicación"

Las subclases deben **poder sustituir a sus clases base** sin cambiar el comportamiento esperado del programa.

### ❌ Violación del LSP

```kotlin
// MAL - Violación del LSP
open class Bird {
    open fun fly() {
        println("Flying...")
    }
}

class Sparrow : Bird() {
    override fun fly() {
        println("Sparrow flying!")
    }
}

class Penguin : Bird() {
    override fun fly() {
        // ❌ Los pingüinos no vuelan!
        throw UnsupportedOperationException("Penguins can't fly!")
    }
}

// Uso
fun makeBirdFly(bird: Bird) {
    bird.fly()  // ❌ Explota si bird es Penguin
}

fun main() {
    makeBirdFly(Sparrow())  // ✅ Funciona
    makeBirdFly(Penguin())  // ❌ Lanza excepción
}
```

**Problema**: `Penguin` no puede sustituir a `Bird` sin romper la aplicación.

### ✅ Aplicando LSP

```kotlin
// BIEN - Separación basada en comportamiento real

interface Animal {
    fun eat()
}

interface Flyable {
    fun fly()
}

interface Swimmable {
    fun swim()
}

// Gorrión: vuela y come
class Sparrow : Animal, Flyable {
    override fun eat() {
        println("Sparrow eating seeds")
    }

    override fun fly() {
        println("Sparrow flying!")
    }
}

// Pingüino: nada y come (NO vuela)
class Penguin : Animal, Swimmable {
    override fun eat() {
        println("Penguin eating fish")
    }

    override fun swim() {
        println("Penguin swimming!")
    }
}

// Uso seguro
fun feedAnimal(animal: Animal) {
    animal.eat()  // ✅ Todos los animales comen
}

fun makeFly(flyable: Flyable) {
    flyable.fly()  // ✅ Solo objetos Flyable
}

fun main() {
    val sparrow = Sparrow()
    val penguin = Penguin()

    feedAnimal(sparrow)  // ✅ Funciona
    feedAnimal(penguin)  // ✅ Funciona

    makeFly(sparrow)     // ✅ Funciona
    // makeFly(penguin)  // ❌ Error de compilación (correcto!)
}
```

**Beneficios**:
- ✅ No hay sorpresas en tiempo de ejecución
- ✅ Las interfaces reflejan comportamientos reales
- ✅ Type-safe: errores en compile-time

### 🏗️ LSP en Nuestra Arquitectura

```kotlin
// ✅ CORRECTO - Cualquier Result<T> se puede sustituir

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}

// Uso consistente en toda la app
suspend fun getPokemonList(): Result<List<Pokemon>> {
    return Result.Success(listOf(...))  // ✅ Es un Result
}

suspend fun getPokemonDetail(): Result<PokemonDetail> {
    return Result.Error(Exception())    // ✅ Es un Result
}

// El ViewModel puede manejar cualquier Result<T> de forma uniforme
fun <T> handleResult(result: Result<T>) {
    when (result) {
        is Result.Success -> showData(result.data)
        is Result.Error -> showError(result.exception)
        is Result.Loading -> showLoading()
    }
}
```

---

## 4️⃣ I - Interface Segregation Principle (ISP)

### Definición
> "Los clientes no deben verse obligados a depender de interfaces que no utilizan"

Las interfaces deben ser **específicas y pequeñas**, no grandes interfaces "todo en uno" (God Interface).

### ❌ Violación del ISP

```kotlin
// MAL - Interfaz demasiado grande (God Interface)
interface Worker {
    fun work()
    fun eat()
    fun sleep()
    fun takeVacation()
    fun getSalary(): Double
    fun attendMeeting()
}

// Robot trabajador NO necesita eat, sleep, vacation
class RobotWorker : Worker {
    override fun work() {
        println("Robot working...")
    }

    // ❌ Implementaciones forzadas sin sentido
    override fun eat() {
        throw UnsupportedOperationException("Robots don't eat")
    }

    override fun sleep() {
        throw UnsupportedOperationException("Robots don't sleep")
    }

    override fun takeVacation() {
        throw UnsupportedOperationException("Robots don't take vacation")
    }

    override fun getSalary(): Double {
        return 0.0  // Robots don't get paid
    }

    override fun attendMeeting() {
        throw UnsupportedOperationException("Robots don't attend meetings")
    }
}
```

**Problema**: `RobotWorker` se ve forzado a implementar métodos que no necesita.

### ✅ Aplicando ISP

```kotlin
// BIEN - Interfaces pequeñas y específicas

interface Workable {
    fun work()
}

interface Eatable {
    fun eat()
}

interface Sleepable {
    fun sleep()
}

interface Payable {
    fun getSalary(): Double
}

interface Vacationable {
    fun takeVacation()
}

// Humano implementa todo
class HumanWorker : Workable, Eatable, Sleepable, Payable, Vacationable {
    override fun work() {
        println("Human working...")
    }

    override fun eat() {
        println("Human eating...")
    }

    override fun sleep() {
        println("Human sleeping...")
    }

    override fun getSalary(): Double {
        return 50000.0
    }

    override fun takeVacation() {
        println("Human on vacation!")
    }
}

// Robot solo implementa lo que necesita
class RobotWorker : Workable {
    override fun work() {
        println("Robot working 24/7...")
    }
}
```

**Beneficios**:
- ✅ Cada clase implementa solo lo que necesita
- ✅ Interfaces cohesivas y con propósito claro
- ✅ Más fácil de entender y mantener

### 🏗️ ISP en Nuestra Arquitectura

```kotlin
// ✅ CORRECTO - Interfaz específica y cohesiva

// Interfaz pequeña y enfocada
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
    // Solo métodos relacionados con Pokemon
}

// ❌ INCORRECTO - God Interface
interface BadRepository {
    // Pokemon
    suspend fun getPokemonList(): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>

    // Items (no relacionado con Pokemon)
    suspend fun getItems(): Result<List<Item>>

    // Moves (no relacionado con Pokemon)
    suspend fun getMoves(): Result<List<Move>>

    // User preferences (completamente diferente)
    suspend fun saveUserPreference(key: String, value: String)
    suspend fun getUserPreference(key: String): String?
}

// ✅ MEJOR - Interfaces segregadas
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
}

interface ItemRepository {
    suspend fun getItems(): Result<List<Item>>
}

interface MoveRepository {
    suspend fun getMoves(): Result<List<Move>>
}

interface PreferencesRepository {
    suspend fun savePreference(key: String, value: String)
    suspend fun getPreference(key: String): String?
}
```

---

## 5️⃣ D - Dependency Inversion Principle (DIP)

### Definición
> "Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones. Las abstracciones no deben depender de detalles. Los detalles deben depender de abstracciones."

En otras palabras:
1. **Depende de interfaces, no de implementaciones concretas**
2. **Las capas de alto nivel definen las interfaces que las capas de bajo nivel implementan**

### ❌ Violación del DIP

```kotlin
// MAL - Acoplamiento directo a implementación concreta

// Bajo nivel: Base de datos MySQL
class MySQLDatabase {
    fun connect() {
        println("Connecting to MySQL...")
    }

    fun query(sql: String): List<String> {
        println("Executing: $sql")
        return listOf("result1", "result2")
    }
}

// Alto nivel: Servicio de usuario DEPENDE de MySQL directamente
class UserService {
    private val database = MySQLDatabase()  // ❌ Acoplamiento fuerte

    fun getUser(id: Int): String {
        database.connect()
        val results = database.query("SELECT * FROM users WHERE id = $id")
        return results.firstOrNull() ?: "User not found"
    }
}
```

**Problemas**:
- ❌ Difícil de testear: necesitamos MySQL real
- ❌ Si queremos cambiar a PostgreSQL, tenemos que modificar `UserService`
- ❌ Alto nivel (`UserService`) depende de bajo nivel (`MySQLDatabase`)

### ✅ Aplicando DIP

```kotlin
// BIEN - Inversión de dependencias

// Abstracción (definida por alto nivel)
interface Database {
    fun connect()
    fun query(sql: String): List<String>
}

// Bajo nivel: Implementaciones concretas DEPENDEN de abstracción
class MySQLDatabase : Database {
    override fun connect() {
        println("Connecting to MySQL...")
    }

    override fun query(sql: String): List<String> {
        println("MySQL executing: $sql")
        return listOf("mysql_result1", "mysql_result2")
    }
}

class PostgreSQLDatabase : Database {
    override fun connect() {
        println("Connecting to PostgreSQL...")
    }

    override fun query(sql: String): List<String> {
        println("PostgreSQL executing: $sql")
        return listOf("postgres_result1", "postgres_result2")
    }
}

// Fake para tests
class FakeDatabase : Database {
    override fun connect() {
        // No-op
    }

    override fun query(sql: String): List<String> {
        return listOf("fake_result")
    }
}

// Alto nivel: Servicio DEPENDE de abstracción (inyección)
class UserService(private val database: Database) {  // ✅ Inyección de dependencia
    fun getUser(id: Int): String {
        database.connect()
        val results = database.query("SELECT * FROM users WHERE id = $id")
        return results.firstOrNull() ?: "User not found"
    }
}

// Uso
fun main() {
    // Producción
    val prodService = UserService(MySQLDatabase())

    // Testing
    val testService = UserService(FakeDatabase())

    // Cambiar a PostgreSQL es trivial
    val postgresService = UserService(PostgreSQLDatabase())
}
```

**Beneficios**:
- ✅ Fácil de testear: inyectamos `FakeDatabase`
- ✅ Cambiar implementación no requiere modificar `UserService`
- ✅ Bajo nivel depende de alto nivel (inversión)

### 🏗️ DIP en Nuestra Arquitectura (¡El Más Importante!)

```
┌──────────────────────────────────────────┐
│         :domain (Alto Nivel)             │
│                                          │
│  interface PokemonRepository {           │ ◄─── Define la interfaz
│      suspend fun getPokemonList()        │
│  }                                       │
└────────────────┬─────────────────────────┘
                 │ depende
                 │
┌────────────────▼─────────────────────────┐
│         :data (Bajo Nivel)               │
│                                          │
│  class PokemonRepositoryImpl(            │ ◄─── Implementa la interfaz
│      private val apiService: PokeApiService
│  ) : PokemonRepository {                 │
│      override suspend fun getPokemonList()
│  }                                       │
└──────────────────────────────────────────┘
```

**Código real**:

```kotlin
// ✅ CORRECTO - DIP aplicado

// 1. Alto nivel (:domain) define la abstracción
// domain/repository/PokemonRepository.kt
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
}

// 2. Bajo nivel (:data) implementa la abstracción
// data/repository/PokemonRepositoryImpl.kt
class PokemonRepositoryImpl(
    private val apiService: PokeApiService  // Detalle de implementación
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        // Implementación con API
    }
}

// 3. Use cases (alto nivel) dependen de abstracción
// domain/usecase/GetPokemonListUseCase.kt
class GetPokemonListUseCase(
    private val repository: PokemonRepository  // ✅ Abstracción, no implementación
) {
    suspend operator fun invoke(limit: Int, offset: Int): Result<List<Pokemon>> {
        return repository.getPokemonList(limit, offset)
    }
}

// 4. Inyección de dependencias (Koin)
val dataModule = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetPokemonListUseCase(get()) }  // Koin inyecta PokemonRepository
}
```

**Beneficios en nuestra app**:
- ✅ `:domain` no depende de `:data` (inversión)
- ✅ Podemos cambiar de Retrofit a Ktor sin tocar `:domain`
- ✅ Fácil de testear: inyectamos `FakePokemonRepository`
- ✅ Podemos tener múltiples implementaciones (API, Room, Mock)

---

## 📊 Resumen Visual: SOLID en PokedexClaude

```
┌─────────────────────────────────────────────────────────┐
│                     SOLID en Acción                     │
└─────────────────────────────────────────────────────────┘

┌─ S: Single Responsibility ────────────────────────────┐
│ Pokemon.kt           → Modelo de datos                │
│ GetPokemonListUseCase → Lógica de negocio            │
│ PokemonRepository     → Contrato de acceso a datos   │
│ PokemonRepositoryImpl → Implementación de datos      │
│ PokemonListViewModel  → Estado de UI                 │
└───────────────────────────────────────────────────────┘

┌─ O: Open/Closed ──────────────────────────────────────┐
│ interface PokemonRepository { ... }                   │
│    ├─ ApiPokemonRepository     (API)                 │
│    ├─ RoomPokemonRepository    (Cache)               │
│    └─ HybridPokemonRepository  (API + Cache)         │
│ ✅ Extensible sin modificar use cases                 │
└───────────────────────────────────────────────────────┘

┌─ L: Liskov Substitution ──────────────────────────────┐
│ Result<Pokemon> = Success | Error | Loading          │
│ ✅ Todos se pueden sustituir sin romper el código     │
└───────────────────────────────────────────────────────┘

┌─ I: Interface Segregation ────────────────────────────┐
│ PokemonRepository  → Solo métodos de Pokemon         │
│ (NO es God Interface con Items, Moves, etc.)         │
│ ✅ Interfaces pequeñas y cohesivas                    │
└───────────────────────────────────────────────────────┘

┌─ D: Dependency Inversion ─────────────────────────────┐
│ :domain define → PokemonRepository (interfaz)        │
│ :data implementa → PokemonRepositoryImpl             │
│ Use cases dependen de → PokemonRepository (abstracción)
│ ✅ Alto nivel no depende de bajo nivel                │
└───────────────────────────────────────────────────────┘
```

---

## 🎯 Conclusión

Los principios SOLID no son reglas estrictas, sino **guías** para escribir mejor código. En PokedexClaude:

- **S**: Cada clase tiene un propósito claro
- **O**: Podemos extender sin modificar (nuevas implementaciones de repositorio)
- **L**: `Result<T>` es sustituible en toda la app
- **I**: Interfaces pequeñas y específicas
- **D**: `:domain` define contratos, `:data` los implementa

### Beneficios Obtenidos
✅ Código más mantenible
✅ Fácil de testear
✅ Cambios aislados
✅ Escalable
✅ Reutilizable

---

**Fecha**: 2026-05-28
**Proyecto**: PokedexClaude
