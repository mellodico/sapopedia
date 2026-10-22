INSERT INTO wiki_biomes (name) VALUES
  ('Amazônia'), ('Caatinga'), ('Cerrado'), ('Mata Atlântica'), ('Pampa'), ('Pantanal');

INSERT INTO wiki_families (scientific_name, description) VALUES
  ('Brachycephalidae', 'Família de sapinhos muito pequenos da Mata Atlântica, com desenvolvimento direto: dos ovos saem filhotes já formados, sem fase de girino.'),
  ('Bufonidae', 'Família dos sapos verdadeiros. Têm pele espessa e rugosa e um par de glândulas paratoides atrás dos olhos, que produzem secreções tóxicas para defesa.'),
  ('Phyllomedusidae', 'Família das pererecas-macaco, que caminham devagar sobre os galhos em vez de saltar. Põem os ovos em folhas dobradas sobre a água, e os girinos caem direto no corpo d''água ao eclodir.');

INSERT INTO wiki_genera (family_id, scientific_name, description) VALUES
  ((SELECT id FROM wiki_families WHERE scientific_name = 'Brachycephalidae'), 'Brachycephalus',
   'Sapinhos diurnos de poucos centímetros, muitos de cores vivas, que vivem na serrapilheira de florestas de altitude.'),
  ((SELECT id FROM wiki_families WHERE scientific_name = 'Bufonidae'), 'Rhinella',
   'Sapos de médio a grande porte, amplamente distribuídos na América do Sul. Inclui os sapos-cururus.'),
  ((SELECT id FROM wiki_families WHERE scientific_name = 'Phyllomedusidae'), 'Pithecopus',
   'Pererecas-macaco de pequeno porte, verdes no dorso e com flancos geralmente alaranjados com listras escuras.');

INSERT INTO wiki_species (genus_id, epithet, common_name, description, habitat, conservation_status) VALUES
  ((SELECT id FROM wiki_genera WHERE scientific_name = 'Rhinella'), 'diptycha', 'Sapo-cururu',
   'Um dos maiores sapos do Brasil, podendo passar de 20 cm. Suas glândulas paratoides produzem uma secreção tóxica para predadores e para animais domésticos que o mordem. Por muito tempo foi chamado de Rhinella schneideri.',
   'Áreas abertas, bordas de mata e até cidades. Reproduz-se em lagoas e poças, onde a fêmea deposita longos cordões de ovos.',
   'LC'),
  ((SELECT id FROM wiki_genera WHERE scientific_name = 'Pithecopus'), 'hypochondrialis', 'Perereca-macaco',
   'Perereca de cerca de 4 cm, verde no dorso, com flancos e coxas alaranjados e listras pretas. Anda devagar pelos galhos, agarrando-os com as mãos, o que lhe rendeu o nome popular.',
   'Vegetação à beira de poças e lagoas temporárias em áreas abertas. Os ovos são colocados em folhas dobradas acima da água.',
   'LC'),
  ((SELECT id FROM wiki_genera WHERE scientific_name = 'Brachycephalus'), 'ephippium', 'Sapinho-pingo-de-ouro',
   'Sapinho de cerca de 2 cm, amarelo-alaranjado intenso. Tem uma placa óssea nas costas e caminha em vez de saltar. A cor viva alerta predadores sobre as toxinas da pele.',
   'Serrapilheira de florestas de altitude da Mata Atlântica, no Sudeste. Os ovos são postos no solo úmido e os filhotes já nascem formados.',
   'LC');

INSERT INTO wiki_species_biomes (species_id, biome_id)
SELECT s.id, b.id
FROM (VALUES
  ('Rhinella', 'diptycha', 'Caatinga'),
  ('Rhinella', 'diptycha', 'Cerrado'),
  ('Rhinella', 'diptycha', 'Pantanal'),
  ('Pithecopus', 'hypochondrialis', 'Amazônia'),
  ('Pithecopus', 'hypochondrialis', 'Caatinga'),
  ('Pithecopus', 'hypochondrialis', 'Cerrado'),
  ('Brachycephalus', 'ephippium', 'Mata Atlântica')
) AS x(genus, epithet, biome)
JOIN wiki_genera g ON g.scientific_name = x.genus
JOIN wiki_species s ON s.genus_id = g.id AND s.epithet = x.epithet
JOIN wiki_biomes b ON b.name = x.biome;

INSERT INTO wiki_glossary_terms (term, slug, term_label, definition) VALUES
  ('Amplexo', 'amplexo', 'substantivo masculino',
   'Abraço reprodutivo em que o macho segura a fêmea pelas costas para fertilizar os ovos à medida que ela os libera.'),
  ('Anuro', 'anuro', 'substantivo masculino · ordem Anura',
   'Nome dado ao grupo de anfíbios sem cauda na fase adulta, que inclui sapos, rãs e pererecas. É a ordem mais numerosa entre os anfíbios.'),
  ('Desenvolvimento direto', 'desenvolvimento-direto', 'expressão',
   'Modo de reprodução em que dos ovos nascem filhotes já com a forma adulta, sem passar pela fase de girino.'),
  ('Girino', 'girino', 'substantivo masculino',
   'Estágio larval dos anfíbios anuros. Vive na água, respira por brânquias e não possui patas, características que mudam ao longo da metamorfose.'),
  ('Glândula paratoide', 'glandula-paratoide', 'substantivo feminino',
   'Par de glândulas salientes atrás dos olhos de muitos sapos, que produzem secreções tóxicas usadas na defesa contra predadores.'),
  ('Metamorfose', 'metamorfose', 'substantivo feminino',
   'Conjunto de transformações pelas quais o girino passa até virar adulto: surgem as patas, a cauda é reabsorvida e as brânquias dão lugar aos pulmões.');


INSERT INTO users (username, name, email, password_hash, bio, created_at) VALUES
  ('ana_herpeto', 'Ana Ribeiro', 'ana.ribeiro@example.com',
   '$2a$10$6iAQLcq2J/UlL0X68lcsguxJLf8lW5aJTaBhGU0md2Z3RVKLDXB7C',
   'Bióloga em formação, apaixonada por pererecas e fotografia noturna.', '2026-10-01 09:12:00'),
  ('bruno_brejo', 'Bruno Tavares', 'bruno.tavares@example.com',
   '$2a$10$gCzpzdAt20GyIkWBiA5eIOI7pkOqPMipBaEMkrAr9AmZRXUKCXeA.',
   'Moro perto de uma lagoa e todo verão aparece sapo no quintal.', '2026-10-02 18:40:00'),
  ('carla_anura', 'Carla Menezes', 'carla.menezes@example.com',
   '$2a$10$NTmM1idAtzoas2XTnLEcJubZjIXIwr7LbHz3dV5gURPciSUybJc4y',
   'Professora de ciências. Uso a SapoPédia nas aulas sobre anfíbios.', '2026-10-03 14:05:00'),
  ('diego_campo', 'Diego Albuquerque', 'diego.albuquerque@example.com',
   '$2a$10$86s332VbBkuMzbknSovwsuRUF2VB3er4LxWCZe7vPtz50cnVqKhDG',
   'Faço trilhas no Cerrado e registro tudo que encontro pelo caminho.', '2026-10-05 07:30:00'),
  ('elisa_bio', 'Elisa Nogueira', 'elisa.nogueira@example.com',
   '$2a$10$LWWEvqk8W6MZpYewk9MiIuLDsOIXU74Fms8zcWPsiJaD4pTOYy4b.',
   '', '2026-10-08 21:15:00');

INSERT INTO wiki_images (species_id, uploaded_by, file_path, alt_text, caption, created_at) VALUES
  ((SELECT s.id FROM wiki_species s JOIN wiki_genera g ON g.id = s.genus_id
    WHERE g.scientific_name = 'Pithecopus' AND s.epithet = 'hypochondrialis'),
   (SELECT id FROM users WHERE username = 'ana_herpeto'),
   'img/foto2.jpg', 'Perereca-macaco verde, de flancos laranja com listras pretas, apoiada em folhas de capim',
   'Perereca-macaco descansando no capim.', '2026-10-01 10:00:00'),
  ((SELECT s.id FROM wiki_species s JOIN wiki_genera g ON g.id = s.genus_id
    WHERE g.scientific_name = 'Pithecopus' AND s.epithet = 'hypochondrialis'),
   (SELECT id FROM users WHERE username = 'ana_herpeto'),
   'img/foto3.jpg', 'Perereca-macaco caminhando sobre um galho à noite',
   'Perereca-macaco andando sobre um galho. Foto: Manoel Augusto.', '2026-10-01 10:05:00'),
  ((SELECT s.id FROM wiki_species s JOIN wiki_genera g ON g.id = s.genus_id
    WHERE g.scientific_name = 'Pithecopus' AND s.epithet = 'hypochondrialis'),
   (SELECT id FROM users WHERE username = 'diego_campo'),
   'img/foto4.jpg', 'Quatro fotos de pererecas-macaco em folhas e caules, e uma desova dentro de uma folha dobrada',
   'Pererecas-macaco e uma desova em folha dobrada.', '2026-10-05 08:00:00');


INSERT INTO forum_topics (user_id, title, type, body, created_at) VALUES
  ((SELECT id FROM users WHERE username = 'ana_herpeto'),
   'Perereca-macaco no quintal depois da chuva', 'avistamento',
   'Ontem à noite, depois da chuva, encontrei uma perereca-macaco andando devagar no galho da goiabeira. Ela não pula, vai agarrando o galho como se fosse um macaquinho! Coloquei as fotos na galeria.',
   '2026-10-10 20:30:00'),
  ((SELECT id FROM users WHERE username = 'bruno_brejo'),
   'Sapo-cururu é perigoso para cachorro?', 'duvida',
   'Meu cachorro vive latindo para um sapo-cururu enorme que aparece no quintal. Li que o veneno dele pode fazer mal. Se o cachorro morder o sapo, o que eu faço?',
   '2026-10-12 19:02:00'),
  ((SELECT id FROM users WHERE username = 'carla_anura'),
   'Sugestão: adicionar o canto de cada espécie', 'sugestao',
   'Seria ótimo se cada página de espécie tivesse uma gravação do canto. Nas aulas, os alunos sempre perguntam como cada sapo "fala", e o canto é a melhor forma de identificar as espécies em campo.',
   '2026-10-14 15:20:00'),
  ((SELECT id FROM users WHERE username = 'diego_campo'),
   'Precisamos falar mais sobre o declínio dos anfíbios', 'opiniao',
   'Os anfíbios estão entre os animais mais ameaçados do planeta, por perda de habitat, poluição e doenças como a quitridiomicose. Acho que a SapoPédia poderia ter uma seção sobre conservação e o que cada um pode fazer.',
   '2026-10-16 08:45:00'),
  ((SELECT id FROM users WHERE username = 'elisa_bio'),
   'Oi, pessoal! Sou nova por aqui', 'outro',
   'Comecei a faculdade de biologia este ano e me apaixonei por anfíbios na disciplina de zoologia. Quero aprender a identificar as espécies da minha região. Alguma dica para quem está começando?',
   '2026-10-18 22:10:00');

INSERT INTO forum_replies (user_id, topic_id, body, created_at)
SELECT u.id, t.id, x.body, x.created_at::timestamp
FROM (VALUES
  ('diego_campo', 'Perereca-macaco no quintal depois da chuva',
   'Que sorte! Fica de olho nas folhas por cima da água, elas dobram a folha para colocar os ovos.', '2026-10-10 21:05:00'),
  ('elisa_bio', 'Perereca-macaco no quintal depois da chuva',
   'As fotos ficaram lindas! Dá para ver bem as listras nas coxas.', '2026-10-19 09:00:00'),
  ('ana_herpeto', 'Sapo-cururu é perigoso para cachorro?',
   'Pode ser sim. Se ele morder, lave a boca dele com bastante água corrente, sem deixar engolir, e leve ao veterinário na hora. E não machuque o sapo, ele ajuda a controlar insetos.', '2026-10-12 19:40:00'),
  ('carla_anura', 'Sapo-cururu é perigoso para cachorro?',
   'Concordo com a Ana. Babar muito e gengiva vermelha são sinais de que o cachorro teve contato com o veneno.', '2026-10-12 20:15:00'),
  ('bruno_brejo', 'Sugestão: adicionar o canto de cada espécie',
   'Apoio! Eu ouço vários cantos aqui na lagoa e não faço ideia de quem é quem.', '2026-10-14 18:00:00'),
  ('elisa_bio', 'Precisamos falar mais sobre o declínio dos anfíbios',
   'Concordo muito. Na faculdade falamos sobre a quitridiomicose e fiquei impressionada com o tamanho do problema.', '2026-10-19 09:20:00'),
  ('ana_herpeto', 'Oi, pessoal! Sou nova por aqui',
   'Bem-vinda! Começa pelo glossário da wiki e anota onde e quando encontrou cada bicho. Isso ajuda muito na identificação.', '2026-10-19 07:50:00'),
  ('diego_campo', 'Oi, pessoal! Sou nova por aqui',
   'Saídas noturnas depois da chuva são as melhores. Leva lanterna e cuidado para não pegar os bichos com as mãos sujas de repelente!', '2026-10-19 08:30:00')
) AS x(username, topic_title, body, created_at)
JOIN users u ON u.username = x.username
JOIN forum_topics t ON t.title = x.topic_title;
