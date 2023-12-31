package CRUD

import ENTITY.Patient
import java.io.*
import java.time.LocalDate


class CRUDPatient(private val filePath: String = "patient.txt") {

    fun createPatient(patient: Patient) {
        try {
            val file = File(filePath)
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            bufferedWriter.write(patient.toString())
            bufferedWriter.newLine()

            bufferedWriter.close()
            fileWriter.close()
            println("¡Paciente creado exitosamente!")
        } catch (e: IOException) {
            println("Error al crear el paciente: ${e.message}")
        }
    }

    fun readPatients(): List<Patient> {
        val patients = mutableListOf<Patient>()

        try {
            val file = File(filePath)
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val patient = parsePatient(line)
                patients.add(patient)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            fileReader.close()
        } catch (e: IOException) {
            println("Error al leer los pacientes: ${e.message}")
        }

        return patients
    }

    fun findPatientByName(name: String): Patient? {
        val patients = readPatients()
        return patients.find { it.name == name }
    }

    /*
    fun updatePatient(oldPatient: Patient, newPatient: Patient) {
        val patients = readPatients().toMutableList()

        val index = patients.indexOfFirst { it.name.equals(oldPatient.name, ignoreCase = true) }

        if (index != -1) {
            patients[index] = newPatient
            try {
                savePatientsToFile(patients)
                println("¡Paciente actualizado exitosamente!")
            } catch (e: IOException) {
                println("Error al actualizar el paciente: ${e.message}")
            }
        } else {
            println("El paciente a actualizar no fue encontrado.")
        }
    }

    */

    fun updatePatientByName(oldName: String, newPatient: Patient) {
        val patients = readPatients().toMutableList()

        val index = patients.indexOfFirst { it.name.equals(oldName, ignoreCase = true) }

        if (index != -1) {
            patients[index] = newPatient
            try {
                savePatientsToFile(patients)
                println("¡Paciente actualizado exitosamente!")
            } catch (e: IOException) {
                println("Error al actualizar el paciente: ${e.message}")
            }
        } else {
            println("El paciente a actualizar no fue encontrado.")
        }
    }




    fun deletePatient(patient: Patient) {
        val patients = readPatients().toMutableList()

        if (patients.remove(patient)) {
            try {
                savePatientsToFile(patients)
                println("¡Paciente eliminado exitosamente!")
            } catch (e: IOException) {
                println("Error al eliminar el paciente: ${e.message}")
            }
        } else {
            println("El paciente a eliminar no fue encontrado.")
        }
    }

    private fun parsePatient(line: String): Patient {
        val parts = line.split(", ")
        return Patient(
            parts[0].split(": ")[1],
            parts[1].split(": ")[1].toInt(),
            parts[2].split(": ")[1],
            parts[3].split(": ")[1],
            LocalDate.parse(parts[4].split(": ")[1]),
            parts[5].split(": ")[1].toDouble(),
            parts[6].split(": ")[1].toBoolean()
        )
    }

    private fun savePatientsToFile(patients: List<Patient>) {
        val file = File(filePath)
        val fileWriter = FileWriter(file, false)
        val bufferedWriter = BufferedWriter(fileWriter)

        for (patient in patients) {
            bufferedWriter.write(patient.toString())
            bufferedWriter.newLine()
        }

        bufferedWriter.close()
        fileWriter.close()
    }
}
