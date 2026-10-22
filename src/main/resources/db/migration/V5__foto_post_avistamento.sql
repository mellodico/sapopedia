DELETE FROM wiki_images WHERE file_path = 'img/foto2.jpg';

INSERT INTO forum_topic_images (topic_id, file_name, position, created_at)
SELECT id, '8f0c03ea834b4c869bdaf8318a98cfdf.jpg', 0, created_at
FROM forum_topics
WHERE title = 'Perereca-macaco no quintal depois da chuva';

UPDATE forum_topics
SET body = 'Ontem à noite, depois da chuva, encontrei uma perereca-macaco no capim do quintal. Ela não pula, vai agarrando as folhas como se fosse um macaquinho! Segue uma das fotos, as outras estão na galeria.'
WHERE title = 'Perereca-macaco no quintal depois da chuva';
