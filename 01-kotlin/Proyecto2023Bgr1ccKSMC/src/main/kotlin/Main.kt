import java.util.*
import kotlin.collections.ArrayList

fun main(){
    println("Hola mundo, te saluda Kevin Maldonado :D")

    //Inmutalbles (no se reasiganan "=")

    val inmutable : String = "Samir";
    //inmutable = "Kevin";

    //Mutables (Re asignar)
    var mutable: String = "Kevin";
    mutable="Samir";

    //Variable primitiva
    val nombre: String = "Kevin Maldonado"
    val  sueldo: Double = 1.2
    val estadoCivil: Char = 'S'
    val mayorEdad: Boolean= true

    //Class Java
    val fechaNacimiento: Date= Date()

    //SWITCH
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C") ->{
            println("Casado")
        }
        "S" ->{
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }
    val coqueteo = if(estadoCivilWhen =="S") "Si" else "No"

    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00)
    calcularSueldo(10.00, 12.00, 20.00)

    //Parametros nombrados
    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00)
    calcularSueldo(10.00, 12.00, bonoEspecial = 20.00)

    calcularSueldo(sueldo = 10.00, bonoEspecial = 20.00) //Named parameters
    calcularSueldo(10.00, bonoEspecial = 20.00) //Named Parameters

    calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=14.00) //Named parameters

    val sumaUno = Suma(1,1)
    val sumaDos = Suma(null,1)
    val sumaTres = Suma(1,null)
    val sumaCuatro = Suma(null, null)
    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)


    //////////////ARREGLOS//////////////

    // Tipos de arreglos


    //Areglos estaticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico)

    //Arreglos dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1,2,3,4,5,6,7,8,9,10
    )
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)


    //FOR EACH -> Unit
    //Iterar un arreglo

    val respuestaForEach: Unit = arregloDinamico
        .forEach{ valorActual : Int ->
            println("Valor actual: ${valorActual}")
        }

    // it (en ingles eso) significa el elemento iterado
    arregloDinamico.forEach{ println(it) }

    arregloEstatico
        .forEachIndexed{ indice: Int, valorActual: Int ->
            println("Valor ${valorActual} Indice: ${indice}")
        }
    println(respuestaForEach)


    //////////////////MAP/////////////////

    //MAP ->Muta el arreglo (Cambia el arreglo)
    // 1) Enviemos el nuevo valor de la iteracion
    // 2) Nos devuleve es un NUEVO ARREGLO con los
    //valores modificados

    val respuestaMap: List<Double> = arregloDinamico
        .map {valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map {it + 15}

    ////////////////////OR y AND/////////////

    //Ambas Devuleven falso o verdador
    //OR -> ANY (Agluno cumple?)
    //AND -> ALL (Todos cumplen?)

    // V AND V=V / V AND F=F
    // v OR V=V / V OR F=V / F OR F

    val respuestaAny : Boolean = arregloDinamico
        .any { valorActual: Int ->
            return@any (valorActual >5)
        }
    println(respuestaAny)  //TRUE

    val respuestaAll : Boolean = arregloDinamico
        .all { valorActual: Int ->
            return@all (valorActual >5)
        }
    println(respuestaAll)  //FALSE




    ///////////////REDUCE/////////////////

    //Reduce = valor acumulado
    //Valor acumulado = 0 (Siempre 0 en lenguaje Kotlin)
    // [1, 2, 3, 4, 5] -> Sumeme todos los valores de arreglo
    // Valor iteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion 1
    // Valor iteracion2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion 2
    // Valor iteracion3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion 3
    // Valor iteracion4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion 4
    // Valor iteracion5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion 5

    val respuestaReduce: Int = arregloDinamico
        .reduce { //acumulado = 0-> SIEMPRE EMPEIZA EN 0
            acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) //-> lOGICA NEGOCIO
        }
    println(respuestaReduce) //78

    ////////////////////FILTER/////////////////

    //FILTER -> Filtrar el arreglo
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo filtrado de forma booleano
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            //Expresion Condicion
            val mayoresACinco : Boolean = valorActual > 5
            return@filter mayoresACinco
        }
    val respuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

}



//////////////CLASES///////////////

abstract  class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno: Int,
        dos: Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract  class  Numeros( //Constructor PRIMARIO
    //Ejemplo:
    //unoProp: Int, // Parametro(Sin modificador de acceso)
    //private var uno: Int, //Propiedad Publica Clase numeros.uno
    //var uno: Int, //Propiedad de la clase (por defecto es PUBLIC)
    //public var uno: Int.
    protected val numeroUno: Int,
    //Propiedad de la clase protected numeros.numeroDos
    protected val numeroDos: Int,
){
    //var cedula: string = "" (public es por defecto)
    //private valorCalculado: int = 0 (private)

    init{ //Bloque codigo constructor primario
        this.numeroUno; this.numeroDos; // this is optional
        numeroUno; numeroDos; // whitout "this", is similar
        println("Inicializando")

    }
}

class Suma( //Constructor primario Suma
    uno: Int, //Parametro
    dos: Int
): Numeros(uno, dos){
    init { //Bloque constructor primario
        this.numeroUno; this.numeroDos;
        this.numeroDos; numeroDos;
    }
    constructor( //Segundo cosntructor
        uno: Int?,
        dos: Int
    ): this ( //llamada constructor primario
        if (uno == null) 0 else uno,
        dos
    ){ // si necesitamos bloque de codigo lo usamos
        numeroUno;
    }

    constructor(//  Tercer constructor
        uno: Int, // parametros
        dos: Int? // parametros
    ) : this(  // llamada constructor primario
        uno,
        if (dos == null) 0 else uno
    )
    // Si no lo necesitamos al bloque de codigo "{}" lo omitimos

   constructor( //cuarto constructor
       uno: Int?,
       dos: Int?
   ): this(
       if (uno == null) 0 else uno,
       if (dos == null) 0 else uno
   )

    // public por defecto, o usar private o protected
    public fun sumar(): Int {
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }
    companion object { // Atributos y Metodos "Compartidos"
        // entre las instancias
        val pi = 3.14
        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorNuevaSuma:Int){
            historialSumas.add(valorNuevaSuma)
        }
    }

}


//////////////FUNCIONES//////////////
//void -> Unit //No devuele nada
fun imprimirNombre(nombre: String): Unit{

    println("Nombre : ${nombre}")
}

fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, //Opcional (defecto)
    bonoEspecial: Double? = null, //Opcion double o puede ser null -> nullable
): Double{
    //bonoEspecial.dec()  -> dice que puede ser null

    // Int -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)

    if(bonoEspecial == null){
        return sueldo * (100/tasa)
    }else{
        bonoEspecial.dec()
        return  sueldo *(100/tasa) + bonoEspecial
    }

}


