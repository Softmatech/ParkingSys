-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Jan 31, 2019 at 06:46 AM
-- Server version: 5.6.38
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parkingDB`
--
CREATE DATABASE IF NOT EXISTS `parkingDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `parkingDB`;

-- --------------------------------------------------------

--
-- Table structure for table `ENTRADA`
--

DROP TABLE IF EXISTS `ENTRADA`;
CREATE TABLE `ENTRADA` (
  `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL,
  `HORA_ENTRADA` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `PLACA` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ENTRADA`
--

INSERT INTO `ENTRADA` (`CODIGO`, `HORA_ENTRADA`, `PLACA`) VALUES
(00000000001, '2018-06-14 02:25:22', 'D-7890-R4'),
(00000000002, '2018-06-14 02:25:57', 'R-489-4893'),
(00000000003, '2018-06-14 02:26:10', 'B-4785958-2'),
(00000000006, '2018-06-20 08:00:00', 'G-4895-5'),
(00000000007, '2018-06-20 08:00:00', 'V-3895-54'),
(00000000008, '2018-06-20 08:00:00', 'B-3892-4'),
(00000000009, '2018-06-20 08:00:00', 'J-390384-4'),
(00000000010, '2018-06-20 08:00:00', 'M-4894-2'),
(00000000011, '2018-06-20 08:00:00', 'B-3949-4'),
(00000000012, '2018-06-20 08:00:00', 'J-3904'),
(00000000013, '2018-06-20 10:00:00', 'H-7865-H9'),
(00000000014, '2018-07-21 15:47:59', 'N57849');

-- --------------------------------------------------------

--
-- Stand-in structure for view `entrada_vs_salida_view`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `entrada_vs_salida_view`;
CREATE TABLE `entrada_vs_salida_view` (
`CODIGO` int(11) unsigned zerofill
,`HORA_ENTRADA` datetime
,`PLACA` varchar(15)
,`HORA_SALIDA` datetime
);

-- --------------------------------------------------------

--
-- Table structure for table `SALIDA`
--

DROP TABLE IF EXISTS `SALIDA`;
CREATE TABLE `SALIDA` (
  `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL,
  `HORA_ENTRADA` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `PLACA` varchar(15) NOT NULL,
  `HORA_SALIDA` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `SALIDA`
--

INSERT INTO `SALIDA` (`CODIGO`, `HORA_ENTRADA`, `PLACA`, `HORA_SALIDA`) VALUES
(00000000001, '2018-06-14 02:25:22', 'D-7890-R4', '2018-06-14 02:28:14'),
(00000000002, '2018-06-14 02:25:57', 'R-489-4893', '2018-07-21 15:57:16'),
(00000000003, '2018-06-14 02:26:10', 'B-4785958-2', '2018-07-21 15:57:18'),
(00000000006, '2018-06-20 08:00:00', 'G-4895-5', '2018-07-21 15:57:21'),
(00000000007, '2018-06-20 08:00:00', 'V-3895-54', '2018-07-21 15:57:23'),
(00000000008, '2018-06-20 08:00:00', 'B-3892-4', '2018-07-21 15:57:26'),
(00000000009, '2018-06-20 08:00:00', 'J-390384-4', '2018-07-21 15:57:31'),
(00000000010, '2018-06-20 08:00:00', 'M-4894-2', '2018-07-21 15:57:28'),
(00000000011, '2018-06-20 08:00:00', 'B-3949-4', '2018-07-21 15:57:33'),
(00000000012, '2018-06-20 08:00:00', 'J-3904', '2018-07-21 15:57:35'),
(00000000013, '2018-06-20 10:00:00', 'H-7865-H9', '2018-07-21 15:48:15'),
(00000000014, '2018-07-21 15:47:59', 'N57849', '2018-12-31 16:59:05');

-- --------------------------------------------------------

--
-- Table structure for table `SALIDA_VS_MONTO`
--

DROP TABLE IF EXISTS `SALIDA_VS_MONTO`;
CREATE TABLE `SALIDA_VS_MONTO` (
  `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL,
  `HORA_ENTRADA` datetime NOT NULL,
  `PLACA` varchar(15) NOT NULL,
  `TIEMPO_CONSUMIDO` varchar(15) DEFAULT NULL,
  `MONTO` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `SALIDA_VS_MONTO`
--

INSERT INTO `SALIDA_VS_MONTO` (`CODIGO`, `HORA_ENTRADA`, `PLACA`, `TIEMPO_CONSUMIDO`, `MONTO`) VALUES
(00000000001, '2018-06-14 02:25:22', 'D-7890-R4', '2mn', 300),
(00000000002, '2018-06-14 02:25:57', 'R-489-4893', '901H31mn', 631400),
(00000000003, '2018-06-14 02:26:10', 'B-4785958-2', '901H31mn', 631400),
(00000000006, '2018-06-20 08:00:00', 'G-4895-5', '751H57mn', 526400),
(00000000007, '2018-06-20 08:00:00', 'V-3895-54', '751H57mn', 526400),
(00000000008, '2018-06-20 08:00:00', 'B-3892-4', '751H57mn', 526400),
(00000000009, '2018-06-20 08:00:00', 'J-390384-4', '751H57mn', 526400),
(00000000010, '2018-06-20 08:00:00', 'M-4894-2', '751H57mn', 526400),
(00000000011, '2018-06-20 08:00:00', 'B-3949-4', '751H57mn', 526400),
(00000000012, '2018-06-20 08:00:00', 'J-3904', '751H57mn', 526400),
(00000000013, '2018-06-20 10:00:00', 'H-7865-H9', '749H47mn', 525000),
(00000000014, '2018-07-21 15:47:59', 'N57849', '3914H10mn', 2740100);

-- --------------------------------------------------------

--
-- Stand-in structure for view `tiempo_precio_view`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `tiempo_precio_view`;
CREATE TABLE `tiempo_precio_view` (
`TIEMPO` varchar(15)
,`PRECIO` double
,`MONEDA` varchar(3)
);

-- --------------------------------------------------------

--
-- Table structure for table `TIEMPO_VS_PRECIO`
--

DROP TABLE IF EXISTS `TIEMPO_VS_PRECIO`;
CREATE TABLE `TIEMPO_VS_PRECIO` (
  `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL,
  `TIEMPO` varchar(15) DEFAULT NULL,
  `PRECIO` double DEFAULT NULL,
  `MONEDA` varchar(3) DEFAULT NULL,
  `REGISTRATION_DAT` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TIEMPO_VS_PRECIO`
--

INSERT INTO `TIEMPO_VS_PRECIO` (`CODIGO`, `TIEMPO`, `PRECIO`, `MONEDA`, `REGISTRATION_DAT`) VALUES
(00000000001, '15 minutos', 300, '$$', '2018-06-14 02:24:32'),
(00000000002, '30 minutos', 500, '$$', '2018-06-14 02:24:38'),
(00000000003, '60 minutos', 700, '$$', '2018-06-14 02:24:43');

-- --------------------------------------------------------

--
-- Structure for view `entrada_vs_salida_view`
--
DROP TABLE IF EXISTS `entrada_vs_salida_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `parkingdb`.`entrada_vs_salida_view`  AS  select `E`.`CODIGO` AS `CODIGO`,`E`.`HORA_ENTRADA` AS `HORA_ENTRADA`,`E`.`PLACA` AS `PLACA`,`S`.`HORA_SALIDA` AS `HORA_SALIDA` from (`parkingdb`.`entrada` `E` left join `parkingdb`.`salida` `S` on(((`E`.`CODIGO` = `S`.`CODIGO`) and (`E`.`HORA_ENTRADA` = `S`.`HORA_ENTRADA`) and (`E`.`PLACA` = `S`.`PLACA`)))) ;

-- --------------------------------------------------------

--
-- Structure for view `tiempo_precio_view`
--
DROP TABLE IF EXISTS `tiempo_precio_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `parkingdb`.`tiempo_precio_view`  AS  select `parkingdb`.`tiempo_vs_precio`.`TIEMPO` AS `TIEMPO`,`parkingdb`.`tiempo_vs_precio`.`PRECIO` AS `PRECIO`,`parkingdb`.`tiempo_vs_precio`.`MONEDA` AS `MONEDA` from `parkingdb`.`tiempo_vs_precio` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ENTRADA`
--
ALTER TABLE `ENTRADA`
  ADD PRIMARY KEY (`CODIGO`,`HORA_ENTRADA`,`PLACA`);

--
-- Indexes for table `SALIDA`
--
ALTER TABLE `SALIDA`
  ADD PRIMARY KEY (`CODIGO`,`HORA_ENTRADA`,`PLACA`),
  ADD KEY `fk_SALIDA_ENTRADA_idx` (`CODIGO`,`HORA_ENTRADA`,`PLACA`);

--
-- Indexes for table `SALIDA_VS_MONTO`
--
ALTER TABLE `SALIDA_VS_MONTO`
  ADD PRIMARY KEY (`CODIGO`,`HORA_ENTRADA`,`PLACA`),
  ADD KEY `fk_SALIDA_VS_MONTO_SALIDA1_idx` (`CODIGO`,`HORA_ENTRADA`,`PLACA`);

--
-- Indexes for table `TIEMPO_VS_PRECIO`
--
ALTER TABLE `TIEMPO_VS_PRECIO`
  ADD PRIMARY KEY (`CODIGO`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ENTRADA`
--
ALTER TABLE `ENTRADA`
  MODIFY `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `TIEMPO_VS_PRECIO`
--
ALTER TABLE `TIEMPO_VS_PRECIO`
  MODIFY `CODIGO` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `SALIDA`
--
ALTER TABLE `SALIDA`
  ADD CONSTRAINT `fk_SALIDA_ENTRADA` FOREIGN KEY (`CODIGO`,`HORA_ENTRADA`,`PLACA`) REFERENCES `ENTRADA` (`CODIGO`, `HORA_ENTRADA`, `PLACA`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `SALIDA_VS_MONTO`
--
ALTER TABLE `SALIDA_VS_MONTO`
  ADD CONSTRAINT `fk_SALIDA_VS_MONTO_SALIDA1` FOREIGN KEY (`CODIGO`,`HORA_ENTRADA`,`PLACA`) REFERENCES `SALIDA` (`CODIGO`, `HORA_ENTRADA`, `PLACA`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
