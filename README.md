# 🃏 InforTactics-UVa

## 📚 Práctica de Fundamentos de Programación (FPRO) - Universidad de Valladolid (UVa)

Este repositorio aloja la implementación del proyecto para la asignatura de **Fundamentos de Programación (FPRO)**, desarrollada para el curso académico 2025-2026 en la Universidad de Valladolid.

**InforTactics-UVa** es una aplicación desarrollada en Java que simula y gestiona un sistema de batallas tácticas basado en mazos de cartas, siguiendo los requisitos específicos detallados en el enunciado de la práctica.

---

## 👥 Autores del Proyecto

La práctica fue realizada por los siguientes alumnos:

* **Alejandro Garcia Lavandera**
* **Beltran Gil Esteban**

---

## 📂 Archivos Clave del Repositorio

| Archivo/Carpeta | Descripción |
| :--- | :--- |
| **`Enunciado.pdf`** | **Documento del Enunciado del Proyecto.** Contiene la descripción oficial, los requisitos detallados y las especificaciones completas de la práctica de FPRO. |
| **`InfortacticsUVa.java`** | **Práctica en Cuestión (Clase Principal).** Contiene el método `main()` y la lógica de interacción del menú y control de flujo principal. |
| `Assets.java` | Clase dedicada a la gestión de recursos estáticos, como la definición de personajes, constantes de juego y dimensiones del tablero. |
| `Methods.java` | Colección de métodos auxiliares y funciones de utilidad, incluyendo la lógica principal del juego/batalla. |
| `Barajas/` | Carpeta contenedora de archivos de datos como `BarajasEnemigas.txt` y `BarajaGuardada.txt`. |

---

## 💡 Contenido y Lógica del Proyecto

### Clase Principal: `InfortacticsUVa.java`

El archivo `InfortacticsUVa.java` actúa como el **controlador de flujo** y la **interfaz de usuario (IU)** del juego. Gestiona el menú principal, la configuración de las barajas, las opciones de carga/guardado y el inicio de las partidas (PvE y PvP).

#### Funciones Clave y Lógica Implementada

El código está estructurado en torno a varias funciones que manejan la interacción y la visualización:

| Método | Propósito y Funcionamiento Clave |
| :--- | :--- |
| **`configureDeck(Scanner in, ...)`** | **Método central de configuración.** Permite al jugador añadir un personaje con el formato `SXY` (Símbolo, Fila, Columna) o borrarlo con la opción `X` seguido de `XY`. **Valida** el coste de Elixir y que la posición esté dentro de la zona de despliegue permitida (ej. filas 3-5 en PvE). |
| **`printBoard(String[] deck)`** | Dibuja la cuadrícula del tablero de juego. Utiliza los datos del array `deck` para colocar los emojis en sus coordenadas. **Diferencia visualmente las zonas de despliegue** (rojo para enemigo/J1, cian para jugador/J2). |
| **`loadRandomEnemyDeck()`** | Lee el archivo `BarajasEnemigas.txt`. Primero cuenta las barajas válidas y luego **selecciona una baraja aleatoria** para cargarla como el oponente en el modo PvE. |
| `main()` / `logica...` | La serie de métodos `logicaNuevaPartida`, `logicaPvP`, etc., gestionan las opciones seleccionadas en el menú principal (`printMenu`) y controlan el flujo entre la configuración y el inicio de la batalla. |
| `calculateCurrentElixir()` | Calcula el Elixir restante restando el coste total de los personajes colocados del Elixir inicial (`Assets.INITIAL_ELIXIR`). |
| `saveDeck()` / `loadDeck()` | Implementa la persistencia de datos, permitiendo guardar y cargar la baraja del jugador desde `BarajaGuardada.txt`. |

***

## ⚙️ Tecnologías

* **Lenguaje de Programación:** Java
* **Compilación y Ejecución:** JDK (versión requerida por la asignatura)

---

## 🚀 Instalación y Uso

### Requisitos

Asegúrate de tener instalado el **Java Development Kit (JDK)** en tu sistema.

### 1. Clonar el Repositorio

```bash
git clone [https://github.com/itsalexxd/InforTactics-UVa.git](https://github.com/itsalexxd/InforTactics-UVa.git)
cd InforTactics-UVa
