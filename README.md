<div align="center">
  
  # Cronometro de Eventos App Móvil
</div>

<div align="center">
  
  ![GitHub](https://img.shields.io/github/last-commit/bnphony/Cronometro-AS)
  [![Java](https://img.shields.io/badge/Code-Java-blue)](https://www.java.com/es/)
</div>

## Indice
- [Cronometro de Eventos App Móvil](#cronometro-de-eventos-app-móvil)
  - [Descripción](#descripción)
    - [Tecnologías](#tecnologías)
  - [Dominio](#dominio)
    - [Cliente](#cliente)
    - [Producto](#producto)
    - [Venta](#venta)
    - [Productos Vendidos](#productos-vendidos)
    - [Usuario](#usuario)
  - [Funciones](#funciones)
    - [Probar la Aplicación](#probar-la-aplicación)
  - [Autor](#autor)
    - [Contacto](#contacto)
  - [Licencia de Uso](#licencia-de-uso)

## Descripción
Este proyecto es una Aplicación Móvil para establecer y visualizar el tiempo limite hasta que se cumpla un evento. Creada en Android Studio, utilizando el lenguage Java.
Las funciones principales:
- Inicio de sesión, mantiene activa la sesión.
- Creación de una cuenta de Usuario.
- CREATE, LIST, UPDATE, DELETE eventos.
- Uso de fragmentos para agilizar el desplazamiento entre pantallas.
- Almacenamiento de images.
- Visualización del tiempo limite en tiempo real.
- Utilización de CardView y RecyclerView.
   
### Tecnologías

- Lenguaje de Programación: [Java](https://www.java.com/es/) - Lenguaje predeterminado de Android Studio.
- Base de Datos: [SQLite3](https://developer.android.com/tools/sqlite3?hl=es-419) - Administrar la base de datos de la aplicación.
- Material Design: EditText - Android.material:1.0.0
- Menú Principal: [Chip Navigation](https://github.com/ismaeldivita/chip-navigation-bar) - Menú Flotante para cambiar entre fragmentos.
- Lista de Eventos: RecyclerView:1.1.0 y CardView:1.0.0
- Calcular el tiempo restante: [threetenbp](https://github.com/JakeWharton/ThreeTenABP) : 1.3.0
- Diseño de PIN de acceso: [pinview](https://github.com/ChaosLeung/PinView) : 1.4.3 - crear interfaz para insersar clave de acceso mediante el uso de un PIN.
  
## Dominio

Gestionar usuarios, y eventos, sabemos que:

- Un evento es registrado por un usuario, tiene su descripcion, fecha y hora limite, y una imagen que lo represente.
- Un usuario puede crearse una cuenta para acceder al sistema, iniciar sesión, editar su perfil, cambiar y recuperar su contraseña..

### Evento

| Campo       | Tipo    | Descripción            |
|-------------|---------|------------------------|
| id          | UUID    | Identificar único      |
| titulo      | text    | Título del Evento      |
| descripcion | text    | Descripción del Evento |
| f_final     | text    | Fecha Final del Evento |
| hora_final  | text    | Hora Final del evento  |
| imagen      | blob    | Imagen del Evento      |
| estado      | text    | Estado del Evento      |
| fk_usuario  | Usuario | Usuario del Evento     |

### Usuario

| Campo     | Tipo | Descripción              |
|-----------|------|--------------------------|
| id        | UUID | Identificador único      |
| nombre    | text | Nombre del Usuario       |
| user_name | text | Nombre único del Usuario |
| email     | text | Email del Usuario        |
| password  | text | Contraseña del Usuario   |


## Funciones
<table>
  <tr>
    <td witdh="50%">
      <h3 align="center">Splash Screen</h3>
      <div align="center">
        <img src="./img_ventas/splash_screen.png" width="200" alt="Splash Screen">
        <p>
          - Primera pantalla de la aplicación, animación de cierre entre el icono de la aplicación y el nombre de la misma.
        </p>
      </div>
    </td>
    <td witdh="50%">
      <h3 align="center">Inicio de Sesión</h3>
      <div align="center">
        <img src="./img_ventas/login.png" width="200" alt="Splash Screen">
        <p>
          - Pantalla de Inicio de Sesión, solo pueden ingresar usuarios que esten registrados en la base de datos.<br/>
          - Opción de mantener activa la sesión, incluso si se cierra la aplicación.
        </p>
      </div>
    </td>
  </tr>
  
  <tr>
    <td witdh="50%">
      <h3 align="center">Crear un Usuario</h3>
      <div align="center">
        <img src="./img_ventas/crear_usuario.png" width="200" alt="Splash Screen">
        <p>
          - Crear una cuenta de usuario para acceder a la aplicación. Campo de confirmación de contraseña.
        </p>
      </div>
    </td>
    <td witdh="50%">
      <h3 align="center">Menú Principal</h3>
      <div align="center">
        <img src="./img_ventas/main_menu.png" width="200" alt="Splash Screen">
        <p>
          - Saludo de bienvenida al usuario. Opciones: Gestión de Clientes, Gestión de Productos, Gestión de Ventas y Opción para Cerrar Sesión. 
        </p>
      </div>
    </td>
  </tr>

  
  <tr>
    <td witdh="50%">
      <h3 align="center">Gestión de Clientes</h3>
      <div align="center">
        <img src="./img_ventas/create_client.png" width="200" alt="Splash Screen">
        <p>
          - CREATE, LIST, UPDATE, DELETE clientes.
        </p>
      </div>
    </td>
    <td witdh="50%">
      <h3 align="center">Gestión de Productos</h3>
      <div align="center">
        <img src="./img_ventas/create_product.png" width="200" alt="Splash Screen">
        <p>
          - CREATE, LIST, UPDATE, DELETE productos. </br>
        - La fecha de caducidad no puede ser menor que la fecha actual. 
        </p>
      </div>
    </td>
  </tr>

  <tr>
    <td witdh="100%" colspan="2">
      <h3 align="center">Gestión Ventas</h3>
      <div align="center">
        <img src="./img_ventas/create_sale.png" width="200" alt="Splash Screen">
        <p>
          - CREATE, LIST Ventas
          - Uso de Dialog para seleccionar el producto y la cantidad a vender.<br/>
          - Seleccionar el cliente de la venta.
        </p>
      </div>
    </td>
    
  </tr>
</table>

### Probar la Aplicación

[Descargar la aplicación](https://github.com/bnphony/Ventas-AS/tree/master/img_ventas/app.apk)



## Autor
Codificado por [Bryan Jhoel Tarco Taipe](https://github.com/bnphony)

### Contacto
<a href="https://www.linkedin.com/in/bryan-tarco01" rel="noopener noreferrer" target="_blank">
  <img align="center" src="https://github.com/bnphony/Portafolio/blob/deployed/static/img/linkedin_icon.png" alt="LinkedIn" height="40" width="40" />
</a>
<a href="https://github.com/bnphony" rel="noopener noreferrer" target="blank">
  <img align="center" src="https://github.com/bnphony/Portafolio/blob/deployed/static/img/github_icon.png" alt="GitHub" height="40" width="40" />
</a>
<a href="mailto: bryan.tarco01@gmail.com" target="_blank">
  <img align="center" src="https://github.com/bnphony/Portafolio/blob/deployed/static/img/email_icon.png" alt="Email" height="40" width="40" />
</a>



## Licencia de Uso
Este repositorio y todo su contenido está licenciado bajo licencia **Creative Commons**. Por favor si compartes, usas o modificas este proyecto cita a su
autor, y usa las mismas condiciones para su uso docente, formativo o educativo y no comercial.
