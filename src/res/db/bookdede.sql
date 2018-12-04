-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-12-2018 a las 22:00:14
-- Versión del servidor: 10.1.36-MariaDB
-- Versión de PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bookdede`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `isbn` varchar(17) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `autor` varchar(40) COLLATE utf8_spanish_ci NOT NULL,
  `editorial` varchar(25) COLLATE utf8_spanish_ci DEFAULT NULL,
  `genero` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `fechaPublicacion` date DEFAULT NULL,
  `valoracion` int(11) DEFAULT NULL,
  `sinopsis` varchar(400) COLLATE utf8_spanish_ci DEFAULT NULL,
  `pdf` blob,
  `portada` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `listaslibros`
--

CREATE TABLE `listaslibros` (
  `email` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `isbn` varchar(17) COLLATE utf8_spanish_ci DEFAULT NULL,
  `pendientes` tinyint(1) DEFAULT NULL,
  `favoritos` tinyint(1) DEFAULT NULL,
  `leidos` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `provincias`
--

CREATE TABLE `provincias` (
  `id` int(10) UNSIGNED NOT NULL,
  `slug` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `provincia` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `comunidad_id` int(10) UNSIGNED NOT NULL,
  `capital_id` int(11) NOT NULL DEFAULT '-1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 CHECKSUM=1 COLLATE=utf8_spanish_ci DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

--
-- Volcado de datos para la tabla `provincias`
--

INSERT INTO `provincias` (`id`, `slug`, `provincia`, `comunidad_id`, `capital_id`) VALUES
(1, 'alava', 'Álava', 16, 46),
(2, 'albacete', 'Albacete', 8, 54),
(3, 'alicante', 'Alicante', 10, 152),
(4, 'almeria', 'Almería', 1, 292),
(5, 'avila', 'Ávila', 7, 395),
(6, 'badajoz', 'Badajoz', 11, 644),
(7, 'illes-balears', 'Illes Balears', 4, 836),
(8, 'barcelona', 'Barcelona', 9, 881),
(9, 'burgos', 'Burgos', 7, 1220),
(10, 'caceres', 'Cáceres', 11, 1580),
(11, 'cadiz', 'Cádiz', 1, 1776),
(12, 'castellon', 'Castellón', 10, 1844),
(13, 'ciudad-real', 'Ciudad Real', 8, 1978),
(14, 'cordoba', 'Córdoba', 1, 2065),
(15, 'a-coruna', 'A Coruña', 12, 2150),
(16, 'cuenca', 'Cuenca', 8, 2285),
(17, 'girona', 'Girona', 9, 2526),
(18, 'granada', 'Granada', 1, 2747),
(19, 'guadalajara', 'Guadalajara', 8, 2947),
(20, 'guipuzcoa', 'Guipúzcoa', 16, 3159),
(21, 'huelva', 'Huelva', 1, 3257),
(22, 'huesca', 'Huesca', 2, 3396),
(23, 'jaen', 'Jaén', 1, 3545),
(24, 'leon', 'León', 7, 3676),
(25, 'lleida', 'Lleida', 9, 3918),
(26, 'la-rioja', 'La Rioja', 17, 4124),
(27, 'lugo', 'Lugo', 12, 4238),
(28, 'madrid', 'Madrid', 13, 4356),
(29, 'malaga', 'Málaga', 1, 4523),
(30, 'murcia', 'Murcia', 14, 4588),
(31, 'navarra', 'Navarra', 15, 4815),
(32, 'ourense', 'Ourense', 12, 4925),
(33, 'asturias', 'Asturias', 3, 5009),
(34, 'palencia', 'Palencia', 7, 5137),
(35, 'las-palmas', 'Las Palmas', 5, 5252),
(36, 'pontevedra', 'Pontevedra', 12, 5312),
(37, 'salamanca', 'Salamanca', 7, 5588),
(38, 'santa-cruz-de-tenerife', 'Santa Cruz de Tenerife', 5, 5732),
(39, 'cantabria', 'Cantabria', 6, 5823),
(40, 'segovia', 'Segovia', 7, 6024),
(41, 'sevilla', 'Sevilla', 1, 6152),
(42, 'soria', 'Soria', 7, 6307),
(43, 'tarragona', 'Tarragona', 9, 6499),
(44, 'teruel', 'Teruel', 2, 6721),
(45, 'toledo', 'Toledo', 8, 6934),
(46, 'valencia', 'Valencia', 10, 7219),
(47, 'valladolid', 'Valladolid', 7, 7415),
(48, 'vizcaya', 'Vizcaya', 16, 7489),
(49, 'zamora', 'Zamora', 7, 7821),
(50, 'zaragoza', 'Zaragoza', 2, 8113),
(51, 'ceuta', 'Ceuta', 18, 8115),
(52, 'melilla', 'Melilla', 19, 8116);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `nick` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` varchar(25) COLLATE utf8_spanish_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `pass` varchar(16) COLLATE utf8_spanish_ci NOT NULL,
  `ciudad` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `codPostal` int(5) DEFAULT NULL,
  `genero` varchar(10) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `telefono` varchar(12) COLLATE utf8_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`isbn`);

--
-- Indices de la tabla `listaslibros`
--
ALTER TABLE `listaslibros`
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `isbn` (`isbn`);

--
-- Indices de la tabla `provincias`
--
ALTER TABLE `provincias`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `IDX_provincia` (`provincia`),
  ADD KEY `FK_provincias` (`comunidad_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`email`),
  ADD UNIQUE KEY `nick` (`nick`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `provincias`
--
ALTER TABLE `provincias`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `listaslibros`
--
ALTER TABLE `listaslibros`
  ADD CONSTRAINT `listaslibros_ibfk_1` FOREIGN KEY (`email`) REFERENCES `usuarios` (`email`),
  ADD CONSTRAINT `listaslibros_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `libros` (`isbn`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
