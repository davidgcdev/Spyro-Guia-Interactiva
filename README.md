
# Spyro The Dragon (Guía Multimedia) - Proyecto Android (DAM)

## 📱 Introducción

Este proyecto es una aplicación educativa desarrollada para el módulo de **Programación Multimedia y Dispositivos Móviles (DAM)**. La aplicación sirve como una enciclopedia interactiva sobre el universo de *Spyro The Dragon*, centrada en la integración de contenidos multimedia avanzados, animaciones personalizadas y la gestión de eventos táctiles específicos.

## ✨ Características principales

- **👥 Pestaña Personajes:** Listado de héroes y villanos. Incluye interactividad avanzada en elementos específicos.
- **🌍 Pestaña Mundos:** Guía de los reinos mágicos con un sistema de detección de patrones de clic.
- **💎 Pestaña Coleccionables:** Información sobre los objetos icónicos del juego.
- **🎓 Guía de Inicio Interactiva:** Un sistema de 5 pasos que utiliza *overlays* para bloquear la interfaz y explicar las funcionalidades mediante animaciones de latido y sonidos temáticos. Solo se muestra en la primera ejecución.
- **🥚 Easter Eggs:**
  - **🎬 Vídeo Secreto:** Activado tras realizar 3 clics consecutivos en la misma tarjeta de la pestaña Mundos.
  - **✨ Efecto de Energía Mágica:** Una animación dinámica creada con la clase `Canvas` que se dispara al realizar una pulsación prolongada sobre el villano Ripto.

## 🛠️ Tecnologías utilizadas

Para el desarrollo de las funcionalidades multimedia se han empleado las siguientes tecnologías nativas de Android:

- **🎬 Control de Animaciones:** `ObjectAnimator` y `AnimatorSet` para la coreografía de la guía, y `AnimationUtils` para los efectos de *flash* en las tarjetas.
- **🎨 Gráficos 2D Personalizados:** Clase `Canvas` y `Paint` para la renderización dinámica de la energía del cetro (Apartado E).
- **🔊 Reproducción Multimedia:** `MediaPlayer` para efectos de sonido (.wav) y `VideoView` para la reproducción del Easter Egg de vídeo (.mp4).
- **💾 Persistencia de Datos:** `SharedPreferences` para gestionar el estado de visualización de la guía (Apartado B).
- **🧭 Navegación:** `Navigation Component` con paso de fragmentos y personalización de transiciones.

## 📋 Instrucciones de uso

### 1. Clonar el repositorio

```bash
git clone <URL-del-repositorio>
````

### 2. Configuración y Ejecución

1. Abrir el proyecto en Android Studio (Ladybug 2024.2.1 o superior).
2. Compilar y ejecutar en un emulador o dispositivo físico con **API 31 o superior**.

### 3. Interacción

1. **Guía:** Seguir los pasos de la guía interactiva o pulsar "Saltar guía" en el primer inicio.
2. **Descubrimiento:** Probar 3 clics rápidos sobre la tarjeta de un mundo o realizar una pulsación larga sobre Ripto para activar los secretos multimedia.

## 💭 Conclusiones del desarrollador

### 🎓 Aprendizajes

El desarrollo de esta aplicación ha permitido profundizar en la importancia de la retroalimentación (*feedback*) visual y acústica en la experiencia de usuario. La implementación de una vista personalizada mediante `Canvas` proporcionó una comprensión sólida sobre el ciclo de redibujado de las vistas en Android.

### 🚧 Dificultades

El mayor desafío técnico residió en la sincronización de las animaciones superpuestas de la guía interactiva y la correcta liberación de recursos del `MediaPlayer` para evitar fugas de memoria, asegurando la estabilidad exigida en aplicaciones comerciales.

## 📸 Galería Visual

<div align="center">
  <img src="assets/guia_bienvenida.png" width="160" alt="Bienvenida a la Guía" />
  <img src="assets/guia_personajes.png" width="160" alt="Explicación de Personajes" />
  <img src="assets/guia_completada.png" width="160" alt="Guía Completada" />
  <img src="assets/canvas_ripto.png" width="160" alt="Efecto mágico Canvas" />
  <img src="assets/video_egg.png" width="160" alt="Easter Egg de Vídeo" />
</div>

---

**Curso académico:** 2025/26
**Ciclo formativo:** DAM

```
```
