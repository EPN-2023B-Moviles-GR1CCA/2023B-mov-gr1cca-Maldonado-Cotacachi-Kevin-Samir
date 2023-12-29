package CRUD

import ENTITY.Hospital
import java.io.*
import java.time.LocalDate
class CRUDHospital(private val filePath: String = "hospital.txt") {

    fun createHospital(hospital: Hospital) {
        try {
            val file = File(filePath)
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            bufferedWriter.write(hospital.toString())
            bufferedWriter.newLine()

            bufferedWriter.close()
            fileWriter.close()
            println("¡Hospital creado exitosamente!")
        } catch (e: IOException) {
            println("Error al crear el hospital: ${e.message}")
        }
    }

    fun readHospitals(): List<Hospital> {
        val hospitals = mutableListOf<Hospital>()

        try {
            val file = File(filePath)
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val hospital = parseHospital(line)
                hospitals.add(hospital)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            fileReader.close()
        } catch (e: IOException) {
            println("Error al leer los hospitales: ${e.message}")
        }

        return hospitals
    }

    fun findHospitalByName(name: String): Hospital? {
        val hospitals = readHospitals()
        return hospitals.find { it.name == name }
    }

    fun updateHospital(oldHospital: Hospital, newHospital: Hospital) {
        val hospitals = readHospitals().toMutableList()

        val index = hospitals.indexOfFirst { it.name.equals(oldHospital.name, ignoreCase = true) }

        if (index != -1) {
            hospitals[index] = newHospital
            try {
                saveHospitalsToFile(hospitals)
                println("¡Hospital actualizado exitosamente!")
            } catch (e: IOException) {
                println("Error al actualizar el hospital: ${e.message}")
            }
        } else {
            println("El hospital a actualizar no fue encontrado.")
        }
    }


    fun deleteHospital(hospital: Hospital) {
        val hospitals = readHospitals().toMutableList()

        val index = hospitals.indexOfFirst { it.name.equals(hospital.name, ignoreCase = true) }

        if (index != -1) {
            hospitals.removeAt(index)
            try {
                saveHospitalsToFile(hospitals)
                println("¡Hospital eliminado exitosamente!")
            } catch (e: IOException) {
                println("Error al eliminar el hospital: ${e.message}")
            }
        } else {
            println("El hospital a eliminar no fue encontrado.")
        }
    }


    private fun parseHospital(line: String): Hospital {
        val parts = line.split(", ")
        return Hospital(
            parts[0].split(": ")[1],
            parts[1].split(": ")[1].toInt(),
            parts[2].split(": ")[1],
            LocalDate.parse(parts[3].split(": ")[1]),
            parts[4].split(": ")[1].toBoolean()
        )
    }

    private fun saveHospitalsToFile(hospitals: List<Hospital>) {
        val file = File(filePath)
        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)

        for (hospital in hospitals) {
            bufferedWriter.write(hospital.toString())
            bufferedWriter.newLine()
        }

        bufferedWriter.close()
        fileWriter.close()
    }
}

