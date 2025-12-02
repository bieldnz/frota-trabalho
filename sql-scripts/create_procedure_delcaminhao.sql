DELIMITER //

CREATE PROCEDURE apagar_caminhao(
   IN p_caminhao_id INT
)
BEGIN
    DELETE FROM caminhao WHERE caminhao_id = p_caminhao_id;
    -- Verifica quantas linhas foram afetadas
    IF ROW_COUNT() > 0 THEN
        SELECT 'Registro deletado com sucesso!' AS Mensagem, 1 AS Sucesso;
    ELSE
        SELECT 'Registro não encontrado!' AS Mensagem, 0 AS Sucesso;
    END IF;
END//

DELIMITER ;