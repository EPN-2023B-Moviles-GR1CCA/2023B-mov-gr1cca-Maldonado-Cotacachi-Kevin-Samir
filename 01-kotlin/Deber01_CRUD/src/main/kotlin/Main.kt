
import CRUD.CRUDHospital
import CRUD.CRUDPatient
import ENTITY.Hospital
import ENTITY.Patient
import java.time.LocalDate
import java.util.*


fun main() {
    val scanner = Scanner(System.`in`)
    val hospitalCRUD = CRUDHospital()
    val pacienteCRUD = CRUDPatient()


    loop@ while (true) {
        println("****** ESCOJA LA OPCION QUE DESEA REALIZAR *********")
        println("1. Ver Hospital")
        println("2. Ver Paciente")
        println("3. Salir")
        println("****** Ingrese el número de la opción que desea realizar:")

        val opcionHospital = scanner.nextInt()
        scanner.nextLine()

        when (opcionHospital) {
            1 -> {
                while (true) {
                    println("******* Selecione una opcion para Hospital ******")
                    println("1. Registrar Hospital")
                    println("2. Mostrar todos los Hospitales ")
                    println("3. Mostrar Hospital por Nombre")
                    println("4. Actualizar datos del Hospital")
                    println("5. Eliminar Hospital")
                    println("6. Volver al menú principal")
                    println()
                    println(">>Ingrese la opción deseada:")

                    when (scanner.nextInt()) {
                        1 -> {
                            scanner.nextLine() // Consumir el salto de línea pendiente
                            println("Ingrese el nombre del hospital:")
                            val nombreHospital = scanner.nextLine()

                            println("Ingrese la capacidad de pacientes del hospital:")
                            val capacidadPacientes = scanner.nextInt()
                            scanner.nextLine() // Consumir el salto de línea pendiente

                            println("Ingrese la ubicación del hospital:")
                            val ubicacionHospital = scanner.nextLine()

                            println("Ingrese la fecha de fundación del hospital (formato: yyyy-MM-dd):")
                            val fechaFundacionStr = scanner.nextLine()
                            val fechaFundacion = LocalDate.parse(fechaFundacionStr)

                            println("Hospital público? (True/False):")
                            val esPublico = scanner.nextBoolean()

                            val hospital = Hospital(
                                nombreHospital,
                                capacidadPacientes,
                                ubicacionHospital,
                                fechaFundacion,
                                esPublico)
                            hospitalCRUD.createHospital(hospital)
                        }

                        2 -> {
                            println("**** Mostrando todos los Hospitales ******")
                            val hospitales = hospitalCRUD.readHospitals()
                            hospitales.forEach { println(it) }
                            println("===============================================================================================")
                            println()
                        }
                        3 -> {
                            scanner.nextLine() // Consumir el salto de línea pendiente
                            println("Ingrese el nombre del hospital a buscar:")
                            val nombreBusqueda = scanner.nextLine()
                            val hospitalEncontrado = hospitalCRUD.findHospitalByName(nombreBusqueda)
                            hospitalEncontrado?.let {
                                println("Hospital encontrado: $it")
                            }
                        }

                        4 -> {
                            println("Ingrese el nombre del hospital a actualizar:")
                            val nombreHospital: String = readLine()?.trim() ?: ""

                            println("Nombre ingresado: $nombreHospital")

                            val hospitales = hospitalCRUD.readHospitals()
                            println("Lista de hospitales disponibles:")
                            hospitales.forEach { println(it) }

                            val hospitalAActualizar = hospitales.find { it.name.equals(nombreHospital, ignoreCase = true) }

                            if (hospitalAActualizar != null) {
                                println("Hospital antes de la actualización: $hospitalAActualizar")

                                loop@ while (true) {
                                    println("****** Seleccione el campo que desea actualizar ******")
                                    println("1. Capacidad de pacientes")
                                    println("2. Ubicación")
                                    println("3. Estado público")
                                    println("4. Salir")

                                    when (scanner.nextInt()) {
                                        1 -> {
                                            println("Ingrese la nueva capacidad de pacientes del hospital:")
                                            val nuevaCapacidad = scanner.nextInt()
                                            scanner.nextLine() // Consumir el salto de línea pendiente
                                            hospitalAActualizar.capacityPatient = nuevaCapacidad
                                        }
                                        2 -> {
                                            println("Ingrese la nueva ubicación del hospital:")
                                            val nuevaUbicacion = scanner.nextLine()
                                            hospitalAActualizar.ubication = nuevaUbicacion
                                        }
                                        3 -> {
                                            println("El hospital es público? (true/false):")
                                            val nuevoEsPublico = scanner.nextBoolean()
                                            hospitalAActualizar.isPublic = nuevoEsPublico
                                        }
                                        4 -> break@loop
                                        else -> println("Opción inválida.")
                                    }
                                }

                                hospitalCRUD.updateHospital(hospitalAActualizar, hospitalAActualizar)
                                println("Hospital después de la actualización: $hospitalAActualizar")

                            } else {
                                println("No se encontró un hospital con ese nombre.")
                            }
                        }

                        5 -> {
                            println("Ingrese el nombre del hospital a eliminar:")
                            val nombreHospital: String = readLine()?.trim() ?: ""

                            if (nombreHospital.isNotBlank()) {
                                println("Nombre ingresado: $nombreHospital")

                                val hospitalAEliminar = hospitalCRUD.findHospitalByName(nombreHospital)
                                if (hospitalAEliminar != null) {
                                    hospitalCRUD.deleteHospital(hospitalAEliminar)
                                    println("Hospital eliminado: ${hospitalAEliminar.name}")
                                } else {
                                    println("No se encontró un hospital con ese nombre.")
                                }
                            } else {
                                println("El nombre ingresado no es válido.")
                            }

                        }
                        6 -> break  // Salir del CRUD de hospitales
                        else -> println("Opción inválida.")
                    }
                }
            }
            2 -> {
                while (true) {
                    println("******* Selecione una opcion para Paciente ******")
                    println("1. Registrar Paciente")
                    println("2. Mostrar todos los Pacientes")
                    println("3. Mostrar Paciente por Nombre")
                    println("4. Actualizar Paciente")
                    println("5. Eliminar Paciente por nombre")
                    println("6. Volver al menú principal")
                    println()
                    println(">>Ingrese la opción deseada:")

                    when (scanner.nextInt()) {
                        1 -> {
                            scanner.nextLine() // Consumir el salto de línea pendiente
                            println("Ingrese el nombre del paciente:")
                            val nombrePaciente = readLine()?.trim() ?: ""

                            println("Ingrese la edad del paciente:")
                            val edadPaciente = readLine()?.toIntOrNull() ?: 0

                            println("Ingrese el genero del paciente:")
                            val generoPaciente = readLine()?.trim() ?: ""

                            println("Ingrese el enfermedad del paciente:")
                            val enfermedadPaciente = readLine()?.trim() ?: ""

                            println("Ingrese la fecha de ingreso del paciente (formato: yyyy-MM-dd):")
                            val ingresoPacienteStr = scanner.nextLine()
                            val ingresoPaciente = LocalDate.parse(ingresoPacienteStr)

                            println("Ingrese el peso en kg del paciente:")
                            val pesoPaciente = scanner.nextDouble()

                            println("Tiene alergias? (True/False):")
                            val esAlergico = scanner.nextBoolean()

                            val paciente = Patient(
                                nombrePaciente,
                                edadPaciente,
                                generoPaciente,
                                enfermedadPaciente,
                                ingresoPaciente,
                                pesoPaciente,
                                esAlergico)

                            pacienteCRUD.createPatient(paciente)
                        }
                        2 -> {
                            println("**** Mostrando todos los Pacientes ******")
                            pacienteCRUD.readPatients().forEach { println(it) }
                            println("===============================================================================================")
                            println()
                        }
                        3 -> {
                            scanner.nextLine() // Consumir el salto de línea pendiente
                            println("Ingrese el nombre del paciente a buscar:")
                            val nombreBusqueda = scanner.nextLine()
                            val pacienteEncontrado = pacienteCRUD.findPatientByName(nombreBusqueda)
                            pacienteEncontrado?.let { println(it) }
                        }
                        4 -> {
                            println("Ingrese el nombre del paciente a actualizar:")
                            val nombrePacienteActualizar: String = readLine()?.trim() ?: ""

                            if (nombrePacienteActualizar.isNotBlank()) {
                                println("Nombre ingresado: $nombrePacienteActualizar")

                                val pacientes = pacienteCRUD.readPatients()
                                val pacienteAActualizar = pacientes.find { it.name.equals(nombrePacienteActualizar, ignoreCase = true) }

                                if (pacienteAActualizar != null) {
                                    while (true) {
                                        println("Seleccione el campo que desea actualizar:")
                                        println("1. Nombre del paciente")
                                        println("2. Edad del paciente")
                                        println("3. Género del paciente")
                                        println("4. Enfermedad del paciente")
                                        println("5. Ingreso del paciente")
                                        println("6. Peso del paciente")
                                        println("7. Alergias del paciente")
                                        println("8. Salir del menú de actualización")

                                        when (readLine()?.toIntOrNull() ?: 0) {
                                            1 -> {
                                                println("Ingrese el nuevo nombre del paciente:")
                                                val nuevoNombre = readLine()?.trim() ?: ""
                                                pacienteAActualizar.name = nuevoNombre
                                            }
                                            2 -> {
                                                println("Ingrese la nueva edad del paciente:")
                                                val nuevaEdad = readLine()?.toIntOrNull() ?: 0
                                                pacienteAActualizar.age = nuevaEdad
                                            }
                                            3 -> {
                                                println("Ingrese el nuevo género del paciente:")
                                                val nuevoGenero = readLine()?.trim() ?: ""
                                                pacienteAActualizar.gender = nuevoGenero
                                            }
                                            4 -> {
                                                println("Ingrese la nueva enfermedad del paciente:")
                                                val nuevaEnfermedad = readLine()?.trim() ?: ""
                                                pacienteAActualizar.disease = nuevaEnfermedad
                                            }
                                            5 -> {
                                                println("Ingrese la nueva fecha de ingreso del paciente (formato: yyyy-MM-dd):")
                                                val nuevaFechaIngresoStr = readLine()?.trim() ?: ""
                                                val nuevaFechaIngreso = if (nuevaFechaIngresoStr.isNotBlank()) LocalDate.parse(nuevaFechaIngresoStr) else LocalDate.now()
                                                pacienteAActualizar.dateAdmission = nuevaFechaIngreso
                                            }
                                            6 -> {
                                                println("Ingrese el nuevo peso en kg del paciente:")
                                                val nuevoPeso = readLine()?.toDoubleOrNull() ?: 0.0
                                                pacienteAActualizar.weight = nuevoPeso
                                            }
                                            7 -> {
                                                println("El paciente sufre de alergias? (True/False):")
                                                val nuevasAlergias = readLine()?.toBoolean() ?: false
                                                pacienteAActualizar.haveAllergies = nuevasAlergias
                                            }
                                            8 -> break
                                            else -> println("Opción inválida.")
                                        }
                                    }
                                    println("Paciente después de la actualización: $pacienteAActualizar")
                                    pacienteCRUD.updatePatient(pacienteAActualizar, pacienteAActualizar)
                                } else {
                                    println("No se encontró un paciente con ese nombre.")
                                }
                            } else {
                                println("El nombre ingresado no es válido.")
                            }
                        }

                        5 -> {
                            println("Ingrese el nombre del paciente a eliminar:")
                            val nombrePacienteEliminar: String = readLine()?.trim() ?: ""

                            if (nombrePacienteEliminar.isNotBlank()) {
                                println("Nombre ingresado: $nombrePacienteEliminar")

                                val pacienteAEliminar = pacienteCRUD.findPatientByName(nombrePacienteEliminar)
                                if (pacienteAEliminar != null) {
                                    pacienteCRUD.deletePatient(pacienteAEliminar)
                                    println("Paciente eliminado: ${pacienteAEliminar.name}")
                                } else {
                                    println("No se encontró un paciente con ese nombre.")
                                }
                            } else {
                                println("El nombre ingresado no es válido.")
                            }
                        }

                        6 -> break  // Salir del CRUD de pacientes
                        else -> println("Opción inválida.")
                    }
                }
            }
            3 -> break
            else -> println("Opción inválida.")
        }
    }
    println("¡Gracias por la Interacción!")
}
