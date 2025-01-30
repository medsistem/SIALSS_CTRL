ALTER TABLE `tb_uniatn` ADD INDEX `unidad_tipo` (`F_ClaCli`, `F_Tipo`) USING BTREE;

ALTER TABLE `tb_uniatn` ADD INDEX `tipo` (`F_Tipo`) USING BTREE;

ALTER TABLE `tb_unidis` DROP INDEX `nivel`,
 ADD INDEX `nivel_id1` (`F_NivUniIS`, `F_ClaInt1`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX `nivel_id5` (`F_NivUniIS`, `F_ClaInt5`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX `nivel_id2` (`F_NivUniIS`, `F_ClaInt2`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX `nivel_id3` (`F_NivUniIS`, `F_ClaInt3`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX `nivel_id4` (`F_NivUniIS`, `F_ClaInt4`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX (`F_ClaInt1`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX (`F_ClaInt2`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX (`F_ClaInt3`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX (`F_ClaInt4`) USING BTREE;

ALTER TABLE `tb_unidis` ADD INDEX (`F_ClaInt5`) USING BTREE;

ALTER TABLE `tb_factura` ADD INDEX `unidad_fecha` (`F_ClaCli`, `F_FecEnt`);

ALTER TABLE `tb_factura` ADD INDEX `unidad_clave` (`F_ClaCli`, `F_ClaPro`) USING BTREE;

ALTER TABLE `tb_factura` ADD INDEX `unidad_clave_fecha` (
	`F_ClaCli`,
	`F_ClaPro`,
	`F_FecEnt`
) USING BTREE;

ALTER TABLE `tb_factura` ADD INDEX `uni_lote` (`F_ClaCli`, `F_Lote`);

ALTER TABLE `tb_factura` ADD INDEX `uni_folio` (`F_ClaCli`, `F_ClaDoc`);

ALTER TABLE `tb_factura` ADD INDEX `uni_clave_fol_lote` (
	`F_ClaCli`,
	`F_ClaPro`,
	`F_ClaDoc`,
	`F_Lote`
) USING BTREE;

ALTER TABLE `tb_factura` ADD INDEX `uni_id` (`F_ClaCli`, `F_IdFact`) USING BTREE;

ALTER TABLE `tb_factura` ADD INDEX `uni_clave_fol_lote_id` (
	`F_ClaCli`,
	`F_ClaPro`,
	`F_ClaDoc`,
	`F_Lote`,
	`F_IdFact`
) USING BTREE;

ANALYZE TABLE `tb_uniatn`;
ANALYZE TABLE `tb_unidis`;
ANALYZE TABLE `tb_factura`;