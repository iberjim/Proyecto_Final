# Juego Tres en Raya (Java + MySQL)

Este proyecto es el clásico juego del **Tres en Raya**, programado en Java con ventanas y conectado a una base de datos MySQL para guardar los jugadores y los puntos.

## 🕹️ Funciones del Juego

* **Login y Registro:** Pantalla para crear una cuenta e iniciar sesión antes de jugar.
* **Modos de Juego:** Se puede jugar contra otra persona en el mismo ordenador o contra la máquina (IA).
* **Ranking Global:** Una pantalla que lee la base de datos y muestra la lista de los jugadores con más puntos.

## 📦 Cómo está organizado el código (MVC)

El código está separado en carpetas para que no esté todo mezclado:
* models: Tiene la lógica del juego (los 9 botones y las reglas de quién gana).
* vistas: Son los diseños de las ventanas (el login, el registro y el tablero).
* Controlador: El puente que hace que cuando pulses un botón en la ventana, pase algo en el juego.
* DAO: Los archivos que se encargan de conectar, guardar y leer los datos en MySQL.
