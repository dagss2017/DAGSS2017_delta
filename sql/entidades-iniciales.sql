-- Ajustar el contador de IDs para que no colision con los IDs anteriores
UPDATE USUARIO_GEN set GEN_VAL=100 WHERE GEN_NAME='USUARIO_GEN';


-- Administrador inicial con login "admin" y contraseña "admin"
INSERT INTO `ADMINISTRADOR` (`ID`,`FECHAALTA`,`LOGIN`,`NOMBRE`,`PASSWORD`,`TIPO_USUARIO`,`ULTIMOACCESO`) VALUES 
(1,'2017-11-11 01:04:42','admin','Administrador inicial','ggm44T97GWJ6Rryx3do4yvl1bZ+gmUfG','ADMINISTRADOR','2017-11-11 01:04:42');

		   
-- Medico con dni "11111111A", num. colegiado "11111" y contraseña "11111"
INSERT INTO `CENTROSALUD` (`ID`,`NOMBRE`,`TELEFONO`,`CODIGOPOSTAL`,`DOMICILIO`,`LOCALIDAD`,`PROVINCIA`) VALUES 
(1,'Centro salud pepe','988888888','12345','C/. Pepe, nº 3','Ourense','Ourense');
INSERT INTO `MEDICO` (`ID`,`APELLIDOS`,`DNI`,`EMAIL`,`FECHAALTA`,`NOMBRE`,`NUMEROCOLEGIADO`,`PASSWORD`,`TELEFONO`,`TIPO_USUARIO`,`ULTIMOACCESO`,`CENTROSALUD_ID`) VALUES 
(2,'Gomez Gomez','11111111A','a@a.com','2017-11-11 01:04:42','Antonio','11111','lUTQ2zg2voe4Z5OqpITFIjcBziNH10d6','988123456','MEDICO','2017-11-11 01:04:42',1);
	   

--
-- Volcado de datos para la tabla `paciente`
--
-- Paciente con id 2, con dni "22222222B", num. tarjeta sanitaria "2222222222", num seg. social "2222222222222" y contraseña "22222"
INSERT INTO `PACIENTE` (`ID`, `APELLIDOS`, `DNI`, `EMAIL`, `FECHAALTA`, `NOMBRE`, `NUMEROSEGURIDADSOCIAL`, `NUMEROTARJETASANITARIA`, `PASSWORD`, `TELEFONO`, `TIPO_USUARIO`, `ULTIMOACCESO`, `CODIGOPOSTAL`, `DOMICILIO`, `LOCALIDAD`, `PROVINCIA`, `MEDICO_ID`) VALUES
(1, 'Ruiz Granados', '12345678A', 'prg@gmail.com', '2018-06-11 00:00:00', 'Pepe', '1111111111111', '1111111111', NULL, NULL, NULL, NULL, '32000', 'Avenida A', 'A coruña', 'A Coruña', 2),
(2, 'Gómez Gómez', '12345678B', NULL, NULL, 'María', '3333333333333', '3333333333', NULL, NULL, NULL, NULL, '32000', 'Avenida B', 'Lugo', 'Lugo', 2),
(3, 'Benito Carmona', '22222222B', 'b@b.com', '2017-11-11 01:04:42', 'Ana', '2222222222222', '2222222222', 'auJIfVxFAN0IKkDVovGmzfUENiABIC5g', '981123456', 'PACIENTE', '2017-11-11 01:04:42', '12345', 'C/. Benito, nº 2, 4º N', 'Coruña', 'Coruña', 2);

		   
--
-- Volcado de datos para la tabla `cita`
--
INSERT INTO `CITA` (`ID`, `DURACION`, `ESTADO`, `FECHA`, `HORA`, `MEDICO_ID`, `PACIENTE_ID`) VALUES
(1, 15, 'Pendiente', '2018-06-16', '09:00:00', 2, 1),
(2, 10, 'Pendiente', '2018-06-16', '09:15:00', 2, 2),
(3, 10, 'Anulada', '2018-06-16', '09:25:00', 2, 3),
(4, 15, 'Anulada', '2018-06-05', '10:00:00', 2, 1),
(5, 20, 'Pendiente', '2018-06-05', '12:00:00', 2, 3),
(6, 25, 'Pendiente', '2018-06-05', '13:00:00', NULL, NULL);


-- Farmacia con nif "33333333C" y contraseña "33333"
INSERT INTO `FARMACIA`(`ID`,`FECHAALTA`,`NIF`,`NOMBREFARMACIA`,`PASSWORD`,`TIPO_USUARIO`,`ULTIMOACCESO`,`CODIGOPOSTAL`,`DOMICILIO`,`LOCALIDAD`,`PROVINCIA`) VALUES 
(4,'2017-11-11 01:04:42','33333333C','Farmacia de prueba','/QpUw+ZRH3ndoNb3N4gRpT5cz0C7pT9v','FARMACIA','2017-11-11 01:04:42','12345','C/. Farmacia, nº 2,4º N','Coruña','Coruña');


--
-- Volcado de datos para la tabla `medicamento`
--
INSERT INTO `MEDICAMENTO` (`ID`, `FABRICANTE`, `FAMILIA`, `NOMBRE`, `NUMERODOSIS`, `PRINCIPIOACTIVO`) VALUES
(1, 'Tormis SL', 'Vacunas', 'Vacination', 20, 'Acarbosa'),
(2, 'Barnichva', 'Antibiotico', 'Biocura', 50, 'Rentis'),
(3, 'Rendi', 'Analgesico', 'Frenatodo', 30, 'Pharmadop'),
(4, 'Grengon', 'Antiinflamatorio', 'Ibuprofeno', 30, 'Ácido propanoico'),
(5, 'Portuf', 'Antipirético', 'Piremol', 30, 'Escina');


--
-- Volcado de datos para la tabla `prescripcion`
--
INSERT INTO `PRESCRIPCION` (`ID`, `DOSIS`, `FECHAFIN`, `FECHAINICIO`, `INDICACIONES`, `MEDICAMENTO_ID`, `MEDICO_ID`, `PACIENTE_ID`) VALUES
(1, 20, '2018-07-14', '2018-08-08', 'Tomar antes de cada comida', 1, 2, 1),
(2, 10, '2018-07-14', '2018-09-08', 'Tomar después de cada comida', 2, 2, 2),
(3, 10, '2018-07-14', '2018-10-08', 'Tomar dos veces al día', 3, 2, 3),
(4, 30, '2018-07-14', '2018-11-08', 'Tomar antes de dormir', 4, 2, 1);


--
-- Volcado de datos para la tabla `receta`
--
INSERT INTO `RECETA` (`ID`, `CANTIDAD`, `ESTADORECETA`, `FINVALIDEZ`, `INICIOVALIDEZ`, `FARMACIADISPENSADORA_ID`, `PRESCRIPCION_ID`) VALUES
(1, 20, 'Generada', '2018-07-15', '2018-06-16', NULL, 1),
(2, 50, 'Generada', '2018-08-22', '2018-06-17', NULL, 2),
(3, 30, 'Servida', '2018-09-02', '2018-06-18', 4, 3),
(4, 30, 'Anulada', '2018-10-30', '2018-06-19', NULL, 4);


