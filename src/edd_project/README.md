# Cobertura de Sucursales.

    Una reconocida cadena de supermercados busca entrar en Sudamérica, pero necesita un 
    programa que le ayude a determinar la mejor ubicación de sus sucursales. 
    El proposito de este programa es permitirle al usuario seleccionar paradas para colocar sucursales 
    y luego indicar la cobertura comercial. De esta forma, el programa ayudará al usuario a determinar 
    si se logra cubrir las necesidades de la población que hace vida, al menos alrededor de las rutas 
    de transporte urbano. La meta es cubrir enteramente la ciudad.


## Requisitos Previos.

    - Java JDK 21 o superior.
    - Netbeans IDE.
    - Archivo JSON de estaciones con el formato:
    {
        <Nombre de la Red>
            {
                <Nombre de Línea1>:[
                   <Nombre Parada1>,
                   ...
                   {<Nombre Paradai> : <Nombre Paradaj>},
                   ...
                   <Nombre Paradam>
                ]
            },
            ...
            {
                <Nombre de Linean>:[
                   <Nombre Parada1>,
                   ...
                   <Nombre Paradam>
                ]
            }
    }


## Ejecucuión del Programa.

    1. Ejecutar el programa en Netbeans IDE.
    2. Seleccionar cual es el valor de T.
    3. Generar el grafo de estaciones seleccionado a preferencia de usuario.
    4. Elegir la estación inicial para generar el grafo.
    5. Elegir con que vista quieres ver el grafo (BFS o DFS).
    6. Seleccionar el cambio que quiera hacer en el grafo (add or delete branch, add line, ect).

    Obervaciones:
    - Set T: Darle valor a T para poder calcular según lod BFS o DFS.
    - Add Branch: Añadir sucursal en el grafo.
    - Delete Branch: Eliminar sucursal.
    - Branch Coverage: Cobertura de la sucursal.
        Se selecciona el nombre de la sucursal, se agrega y luego se elige el BFS 
        o DFS para ver la cobertura.
    - Total Coverage: Cobertura total de las estaciones.
    - Add Line: Añadir estación al grafo.
        Se introduce la nueva parada. Para agregar la conexión se agrega la nueva parada
        que el usuario haya creado más la conexión (parada ya existente) que quieran crear.


## Instalación.

    Clonar el repositorio de la forma que más le convenga:
    *HTTPS:
        https://github.com/VictorDevCode21/first_project_edd.git

    *SSH:
        git@github.com:VictorDevCode21/first_project_edd.git

    *Github CLI:
        gh repo clone VictorDevCode21/first_project_edd

    
