import java.util.*

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
}


//////////////FUNCIONES/////////////
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
