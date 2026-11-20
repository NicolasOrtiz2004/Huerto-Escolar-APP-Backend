# 📁 model/

Entidades JPA = tablas de MySQL.

| Archivo / carpeta | Qué hace |
|---|---|
| `Sector.java` | Tabla sectores: código único, nombre, descripción, ubicación, estado, responsableId y fechas. |
| `Cultivo.java` | Tabla cultivos: @ManyToOne con Sector (FK sector_id), fechas LocalDate, frecuencia de riego, estado. |
| `EstadoSector.java` | Enum de estados del sector. |
| `EstadoCultivo.java` | Enum de estados del cultivo. |
| `README.md` | Este archivo. |
