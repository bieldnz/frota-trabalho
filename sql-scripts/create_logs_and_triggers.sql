-- Criar tabela de logs para caminhões
CREATE TABLE IF NOT EXISTS Tabela_Log_Caminhao(
    cod_log INT PRIMARY KEY AUTO_INCREMENT,
    cod_caminhao INT,
    alteracao VARCHAR(15),
    data_alt DATETIME DEFAULT CURRENT_TIMESTAMP
);

DELIMITER //

-- Trigger para DELETE
CREATE TRIGGER Log_caminhao_delete
AFTER DELETE 
ON caminhao
FOR EACH ROW
BEGIN
    INSERT INTO Tabela_Log_Caminhao 
        (cod_caminhao, alteracao, data_alt)
    VALUES (OLD.caminhao_id, 'DELETE', NOW());
END //

-- Trigger para INSERT
CREATE TRIGGER Log_caminhao_insert
AFTER INSERT
ON caminhao
FOR EACH ROW
BEGIN
    INSERT INTO Tabela_Log_Caminhao 
        (cod_caminhao, alteracao, data_alt)
    VALUES (NEW.caminhao_id, 'INSERT', NOW());
END //

-- Trigger para UPDATE
CREATE TRIGGER Log_caminhao_update
AFTER UPDATE
ON caminhao
FOR EACH ROW
BEGIN
    INSERT INTO Tabela_Log_Caminhao 
        (cod_caminhao, alteracao, data_alt)
    VALUES (NEW.caminhao_id, 'UPDATE', NOW());
END //

DELIMITER ;

-- Procedure para listar logs
DELIMITER $$

CREATE PROCEDURE listar_logs_caminhao (
    IN p_caminhao_id INT,
    IN p_data_inicio DATE,
    IN p_data_fim DATE
)
BEGIN
    SELECT *
    FROM Tabela_Log_Caminhao
    WHERE (p_caminhao_id IS NULL OR cod_caminhao = p_caminhao_id)
      AND (p_data_inicio IS NULL OR DATE(data_alt) >= p_data_inicio)
      AND (p_data_fim IS NULL OR DATE(data_alt) <= p_data_fim)
    ORDER BY data_alt DESC, cod_log DESC;
END$$

DELIMITER ;