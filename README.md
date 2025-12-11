# Asistencia Clases - Proyecto Final Android

Este proyecto es una aplicación Android para el registro de asistencia a clases, desarrollada como trabajo final para la materia de Programación II.

## Características

*   **Arquitectura MVVM:** Implementación de Model-View-ViewModel con LiveData y Coroutines.
*   **Base de Datos Local (Room):** Almacenamiento persistente de registros de asistencia y logs del sistema.
*   **Sincronización (Retrofit):** Envío de datos a un servidor remoto (API REST).
*   **Cámara (CameraX):** Captura de fotos de evidencia para cada registro de asistencia.
*   **Logs de Sistema:** Registro automático de eventos (creación, sincronización, errores).
*   **UI/UX:** Interfaz limpia utilizando RecyclerView y ConstraintLayout.

## Requisitos Previos

*   Android Studio Iguana o superior.
*   JDK 17.
*   Dispositivo Android o Emulador con soporte de cámara (API 24+).

## Configuración

1.  Clonar el repositorio.
2.  Abrir el proyecto en Android Studio.
3.  Sincronizar los archivos Gradle.
4.  Ejecutar la aplicación en un dispositivo o emulador.

## Estructura del Proyecto

*   `data/local`: Entidades de Room (Attendance, AppLog), DAOs y Database.
*   `data/remote`: Interfaz ApiService y cliente Retrofit.
*   `data/repository`: Repositorio único que gestiona la fuente de datos (Local/Remota).
*   `ui/view`: Actividades, Fragmentos y Adaptadores.
*   `ui/viewmodel`: ViewModel y Factory.

## Uso

1.  **Pantalla Principal:** Muestra la lista de asistencias registradas.
    *   Indicador de estado de sincronización (Rojo: No sincronizado, Verde: Sincronizado).
2.  **Agregar Asistencia:**
    *   Presionar el botón flotante (+).
    *   Ingresar el nombre del estudiante.
    *   Tomar una foto con la cámara.
    *   Guardar.
3.  **Sincronizar:**
    *   En el menú de opciones (esquina superior derecha), seleccionar "Sincronizar".
    *   El sistema intentará enviar los registros pendientes al servidor.

## Cumplimiento de Rúbrica

1.  **MVVM:** Implementado en `ui/viewmodel` y `data/repository`.
2.  **Room:** Implementado en `data/local`.
3.  **Retrofit:** Implementado en `data/remote`.
4.  **Cámara:** Implementado en `AddAttendanceFragment` usando CameraX.
5.  **Logs:** Implementado en `AppLog` y `AttendanceRepository`.
6.  **UI/UX:** Uso de RecyclerView, CardView y ConstraintLayout.
7.  **Funcionamiento:** Flujo completo de agregar, listar y sincronizar.

## Autor

[Tu Nombre]
Ingeniería Informática - 2025
